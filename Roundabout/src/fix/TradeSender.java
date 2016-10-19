package fix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import quickfix.Message;
import quickfix.Session;
import quickfix.field.ClOrdID;
import quickfix.field.ExDestination;
import quickfix.field.HandlInst;
import quickfix.field.LocateReqd;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.SymbolSfx;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.fix42.NewOrderSingle;

public class TradeSender {
	
	private static TradeSender tradeCreator = null;
	private TradeSender() {
	}
	
	public static TradeSender getTradeCreator() {
		if (tradeCreator == null)
			tradeCreator = new TradeSender();
		return tradeCreator;
	}
	
	public static boolean allowTrades = true;
	

	private String baseID = null;
	private int ordNum = 0;

	private ClOrdID getNextClOrdID () {
		if (baseID == null) {
			// set day's base ID for orders
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yDDD");
			baseID = df.format(cal.getTime());
			if (baseID.length() > 4)
				baseID = baseID.substring(baseID.length() - 4);
			baseID = baseID + (cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE));
			if (baseID.length() > 7)
				baseID = baseID.substring(0, 7);

			System.out.println("Base ID: \t"+baseID+"\tlenth:\t"+baseID.length());
		}
		
		return new ClOrdID(baseID + (ordNum++));
	}

	private HandlInst handInst = new HandlInst('1');	// always automated execution, no broker intervention
	private TimeInForce tif = new TimeInForce('0');	// always Day

	public static final char BUY = '1', SELL = '2', SHORT = '5';	// SIDES
	public static final char MKT = '1', LIMIT = '2', MOC = '5';	// ORDER TYPES
	private static final int ordIDfieldNum = 11;
	
	private String locateBroker = "BAML";	// TODO set this, or put it in UI
	
	
	public void createAndSendOrder (String ticker, char sideChar, double size, char ordType, double limit, String dest, String suffix) {

		ClOrdID id = getNextClOrdID();
		
		Side side = new Side(sideChar);

		OrdType type = new OrdType(ordType);

		NewOrderSingle order = new NewOrderSingle(id, handInst, new Symbol(ticker), side, new TransactTime(), type);

		if (dest != null)
			order.set(new ExDestination(dest));

		order.set(new OrderQty(size));

		if (suffix != null)
			order.set(new SymbolSfx(suffix));

		if (ordType == LIMIT)
			order.set(new Price(limit));


//		if (tif != null) {
		order.set(tif);
//			System.out.println("\n\nSET TIME IN FORCE : \t" + tif);
//		}

		if (sideChar == SHORT) {					
			order.set(new LocateReqd(false));
			order.setString(5700, locateBroker);
		}
		
		sendOrder(order);
	}
	
	

	HashMap <String, ArrayList<Message>> ordIDtoMessagesOpen = new HashMap<String, ArrayList<Message>> ();


	private void sendOrder (Message order) {
		try {
			String ordID = order.getString(ordIDfieldNum);

			ArrayList<Message> orders = ordIDtoMessagesOpen.get(ordID);
			if (orders == null) {
				orders = new ArrayList<Message>();
				ordIDtoMessagesOpen.put(ordID, orders);
			}

			orders.add(order);


			// check for checkbox enabled
			sendToSession(order);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void sendToSession(Message order) {
		if (orderQueue.offer(order) == false)
			System.err.println("COULD NOT ADD ORDER TO QUEUE!");

		if (orderThread != null)
			return;

		orderThread = new Thread(new OrderSender());
		orderThread.start();
	}


	LinkedBlockingQueue<Message> orderQueue = new LinkedBlockingQueue<>();
	Thread orderThread = null;
	int maxPerSecond = 30;



	// thread to send orders, waiting 1 sec between max size bursts
	class OrderSender implements Runnable {

		Message nextOrder = null;
		int burstOrders = 0;

		public void run() {
			try {
				while (true) {
					if (allowTrades == false) {
						if (orderQueue.size() > 0)
							System.out.println("Waiting for ok to send pending trades : \t" + orderQueue.size());
						Thread.sleep(1000);
						continue;
					}

					nextOrder = orderQueue.poll(1010, TimeUnit.MILLISECONDS);

					// usually null
					if (nextOrder == null) {
						burstOrders = 0;
						// TODO update UI table
						continue;
					}

					Session.sendToTarget(nextOrder);

					burstOrders++;
					if (burstOrders > maxPerSecond) {
						System.out.println("Waiting 2 secs.");

						// TODO update UI 
						Thread.sleep(2000);
						burstOrders = 0;
					}	
				}
			} catch (Exception e) {
				System.err.println("\nORDER SENDER THREAD HAS STOPPED!! \n");
				e.printStackTrace();
			}
		}
	}


}
