package fix;

import java.util.ArrayList;
import java.util.HashMap;

import main.OrdersContainer;
import network.ZMQTradeServer;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.ClOrdID;
import quickfix.fix42.ExecutionReport;
import ui.IListenForUIChanges;

public class ExecutionHandler {


	HashMap<String, String> partialIDs = new HashMap<>();

	ZMQTradeServer tradeServer = ZMQTradeServer.getZMQTradeServer();


	public void handleMessage(ExecutionReport message) {
		try {
			ClOrdID clID = message.getClOrdID();
			String id = clID.getValue();

			char ordStatus = message.getOrdStatus().getValue();

			ArrayList<Message> orders = OrdersContainer.ordIDtoMessagesOpen.get(id);

			if (orders == null) {
				System.err.println("\nNo order found : handleMessage: \t" + message);
				return;
			}

			orders.add(message);

			// ack			// partial fill 
			//			if (ordStatus == '0' || ordStatus == '1') 
			//				return;
			if (ordStatus == '1') {
				//				System.out.println("\nPartial:\t" + message);
				partialIDs.put(id, message.toString());
				notifyPositionsServer(message, id, true);
			}

			listener.notifyOpenOrdersChanged();
			
			// 		filled 			// done for day			// cancelled		// replaced		// rejected		// expired
			if (ordStatus == '2' || ordStatus == '3' || ordStatus == '4' || ordStatus == '5' || ordStatus == '8' || ordStatus == 'C') {

				// if not replaced
				if (ordStatus != '5') {
					OrdersContainer.ordIDtoMessagesClosed.put(id, OrdersContainer.ordIDtoMessagesOpen.remove(id));
					listener.notifyClosedOrdersChanged();
					listener.notifyOpenOrdersChanged();
				}

				// if there is an OrigClOrdID, remove those too
				if (message.isSetField(41)) {
					id = message.getOrigClOrdID().getValue();

					OrdersContainer.ordIDtoMessagesClosed.put(id, OrdersContainer.ordIDtoMessagesOpen.remove(id));
					listener.notifyClosedOrdersChanged();
					listener.notifyOpenOrdersChanged();
				}

				if (ordStatus == '8') {
					listener.notifyClosedOrdersChanged();
					listener.notifyOpenOrdersChanged();

					tradesOpen--;
					tradesRejected++;

					System.err.println("\nRejected:\t" + message);
				}

				if (ordStatus == '2' || ordStatus == '3' || ordStatus == '4') {
					notifyPositionsServer(message, id);

					tradesOpen--;
					if (ordStatus != '4')
						tradesFilled++;
					
				}

				partialIDs.remove(id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		listener.updateStats();
	}



	private void notifyPositionsServer(ExecutionReport msg, String id) {
		notifyPositionsServer(msg, id, false);
	}


	int tradesEntered = 0;
	int tradesFilled = 0;
	int tradesRejected = 0;
	int tradesOpen = 0;
	int sharesTraded = 0;


	int orderSize = 0;
	double price = 0;
	String ticker = null;
	char side = ' ';

	public static final char BUY = '1', SELL = '2', SHORT = '5';	// SIDES
	public static final char MKT = '1', LIMIT = '2', MOC = '5';	// ORDER TYPES


	private void notifyPositionsServer(ExecutionReport msg, String id, boolean partial) {
		if (tradeServer == null) 
			return;

		try {
			orderSize = (int) msg.getCumQty().getValue();
			//			if (size == 0)
			//				return;

			price = msg.getAvgPx().getValue();

			ticker = msg.getSymbol().getValue();
			if (msg.isSetSymbolSfx())
				ticker = ticker + "." + msg.getSymbolSfx().getValue();

			// global tally
			if (partial == false)
				sharesTraded = sharesTraded + orderSize;

			side = msg.getSide().getValue();
			if (side != BUY)
				orderSize = -orderSize;			

		} catch (FieldNotFound e) {
			e.printStackTrace();
			System.err.println("ERROR: MSG NOT SENT TO POSITIONS SERVER:\t"+msg);
			return;
		}


		if (partial)
			ticker = "_"+ticker;

		tradeServer.notifyTrade(ticker, orderSize, price, side);
	}

	IListenForUIChanges listener = null;

	public void setUIListener(IListenForUIChanges uiListener) {
		listener = uiListener;
	}



}
