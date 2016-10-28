package fix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import main.OrdersContainer;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.field.ClOrdID;
import quickfix.field.ExDestination;
import quickfix.field.HandlInst;
import quickfix.field.LocateReqd;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.SymbolSfx;
import quickfix.field.TargetSubID;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.fix42.NewOrderSingle;
import quickfix.fix42.OrderCancelReplaceRequest;
import quickfix.fix42.OrderCancelRequest;
import ui.IListenForUIChanges;
import ui.OrdersTableModel;

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
	
	
	public void createAndSendOrder (String ticker, char sideChar, double size, char ordType, double limit, String dest) {
		ticker = ticker.toUpperCase();
		
		String suffix = null;
		int separatorIndex = ticker.indexOf('.');
		if (separatorIndex < 0)
			separatorIndex = ticker.indexOf('/');
		if (separatorIndex < 0)
			separatorIndex = ticker.indexOf('-');
		
		if (separatorIndex >= 0) {
			suffix = ticker.substring(separatorIndex + 1);
			ticker = ticker.substring(0, separatorIndex);
		}
		
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


		order.set(tif);

		if (sideChar == SHORT) {					
			order.set(new LocateReqd(false));
			order.setString(5700, locateBroker);
		}
		
		setDMAtag(order);
//		setVWAPtag(order, 20, null, null);
		
		sendOrder(order);
	}
	
	
	/** Start Time
* End Time	20161024-20:10:23
* % of Volume*/
	
	private void setVWAPtag(Message message, int percVolume, String startTime, String endTime) {
		message.setString(TargetSubID.FIELD, "ML_ALGO_US");
		String algoParams = "6401=1";
		if (percVolume > 0)
			algoParams = algoParams + ";6403=" + percVolume;
		if (startTime != null)
			algoParams = algoParams  +";6168=" + startTime;
		if (endTime != null)
			algoParams = algoParams + ";126=" + endTime;
		
		message.setString(9999, algoParams + ";9682=v4.3.0BRT;");
	}
	
	
	String defaultTarget ="ML_ARCA";
	
	private void setDMAtag(Message message) {
		message.setString(TargetSubID.FIELD, defaultTarget);
/*
ML_ALGO_US (This is the only route where we can accept the custom tag with algo parameters)
ML_ARCA
ML_DOT
ML_NSDQ
ML_SMARTDMA*/
	}
	
	
	public void cancelOrder(String id) {
		ArrayList<Message> orders = OrdersContainer.ordIDtoMessagesOpen.get(id);
		if (orders == null)
			return;
		
		Message msg = orders.get(orders.size()-1);
		if (msg == null)
			return;

		try {
			String ticker = msg.getString(Symbol.FIELD);
			Side side = new Side(msg.getChar(Side.FIELD));
			OrderCancelRequest cancelOrder = new OrderCancelRequest(new OrigClOrdID(id), getNextClOrdID(), new Symbol(ticker), side, new TransactTime());
			
			if (msg.isSetField(SymbolSfx.FIELD))
				cancelOrder.set(new SymbolSfx(msg.getString(SymbolSfx.FIELD)));
			cancelOrder.set(new OrderQty(msg.getDouble(OrderQty.FIELD)));
			
			sendOrder(cancelOrder);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	public void replaceOrder(String id, double size, double limit) {
		ArrayList<Message> orders = OrdersContainer.ordIDtoMessagesOpen.get(id);
		if (orders == null)
			return;
		
		Message msg = orders.get(orders.size()-1);
		if (msg == null)
			return;

		try {
			String ticker = msg.getString(Symbol.FIELD);
			Side side = new Side(msg.getChar(Side.FIELD));
			OrdType type = new OrdType(msg.getChar(OrdType.FIELD));

			OrderCancelReplaceRequest order = new OrderCancelReplaceRequest(new OrigClOrdID(id), getNextClOrdID(), handInst, new Symbol(ticker), side, new TransactTime(), type);

			if (msg.isSetField(SymbolSfx.FIELD))
				order.set(new SymbolSfx(msg.getString(SymbolSfx.FIELD)));

			order.set(new OrderQty(size));
			if (limit > 0)
				order.set(new Price(limit));
			
			sendOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	



	private void sendOrder (Message order) {
		try {
			String ordID = order.getString(ordIDfieldNum);

			ArrayList<Message> orders = OrdersContainer.ordIDtoMessagesOpen.get(ordID);
			if (orders == null) {
				orders = new ArrayList<Message>();
				OrdersContainer.ordIDtoMessagesOpen.put(ordID, orders);
			}

			orders.add(order);


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

	SessionID sessionID = null;
			

	// thread to send orders, waiting 1 sec between max size bursts
	class OrderSender implements Runnable {

		Message nextOrder = null;
		int burstOrders = 0;

		public void run() {
			try {
				while (true) {
					if (allowTrades == false || sessionID == null) {
						if (orderQueue.size() > 0)
							System.out.println("Waiting for ok to send pending trades : \t" + orderQueue.size());
						Thread.sleep(1000);
						continue;
					}

					nextOrder = orderQueue.poll(1010, TimeUnit.MILLISECONDS);

					// usually null
					if (nextOrder == null) {
						burstOrders = 0;
//						uiListener.notifyOpenOrdersChanged();
						continue;
					}

					Session.sendToTarget(nextOrder, sessionID);

					burstOrders++;
					if (burstOrders > maxPerSecond) {
						System.out.println("Waiting 2 secs.");

						uiListener.notifyOpenOrdersChanged();
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
	
	IListenForUIChanges uiListener = null;
	
	public void addUIListener(IListenForUIChanges listener) {
		uiListener = listener;
	}
	

}
