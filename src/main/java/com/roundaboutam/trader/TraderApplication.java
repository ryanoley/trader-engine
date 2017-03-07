package com.roundaboutam.trader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderBasket;
import com.roundaboutam.trader.order.OrderBasketBook;
import com.roundaboutam.trader.order.OrderBook;
import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.ReplaceOrder;
import com.roundaboutam.trader.ramfix.FIXOrder;
import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderSide;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.ramfix.OrderType;

import com.roundaboutam.trader.execution.ExecutionBook;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import quickfix.field.BeginString;
import quickfix.field.BusinessRejectReason;
import quickfix.field.ClOrdID;
import quickfix.field.ExecID;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.RefMsgType;
import quickfix.field.RefSeqNum;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;
import quickfix.field.Text;

public class TraderApplication implements Application {

    private final ObservableOrder observableOrder = new ObservableOrder();
    private final ObservableMessage observableMessage = new ObservableMessage();
    private final ObservableLogon observableLogon = new ObservableLogon();
    private final ObservableBasket observableBasket = new ObservableBasket();

    private final OrderBook orderBook;
	private final ExecutionBook executionBook;
	private final OrderBasketBook orderBasketBook;

    private final DefaultMessageFactory messageFactory = new DefaultMessageFactory();

	static private final HashMap<SessionID, HashSet<ExecID>> execIDs =  
			new HashMap<SessionID, HashSet<ExecID>>();

	static private final HashSet<SessionID> sessionIDs = new HashSet<SessionID>();

	public TraderApplication(SessionSettings settings) throws ConfigError, FieldConvertError {
		orderBook = new OrderBook();
		executionBook = new ExecutionBook(settings.getString("CustomLogPath"));
		orderBasketBook = new OrderBasketBook();
	}
	
    // Main message handler
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound,
    	IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    	try {
    		SwingUtilities.invokeLater(new MessageProcessor(message, sessionID));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public class MessageProcessor implements Runnable {

    	private final Message message;
        private final SessionID sessionID;

        public MessageProcessor(Message message, SessionID sessionID) {
            this.message = message;
            this.sessionID = sessionID;
        }

        public void run() {
            try {
                MsgType msgType = new MsgType();
                if (message.getHeader().getField(msgType).valueEquals("8")) {
                	executionReport(message, sessionID);
                } else if (message.getHeader().getField(msgType).valueEquals("9")) {
                	orderCancelReject(message, sessionID);
                } else {
                	sendBusinessReject(message, BusinessRejectReason.UNSUPPORTED_MESSAGE_TYPE,
                			"Unsupported Message Type");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            observableMessage.update(message);
        }
    }

    private void sendBusinessReject(Message message, int rejectReason, String rejectText)
            throws FieldNotFound, SessionNotFound {
        Message reply = createMessage(message, MsgType.BUSINESS_MESSAGE_REJECT);
        reverseRoute(message, reply);
        String refSeqNum = message.getHeader().getString(MsgSeqNum.FIELD);
        reply.setString(RefSeqNum.FIELD, refSeqNum);
        reply.setString(RefMsgType.FIELD, message.getHeader().getString(MsgType.FIELD));
        reply.setInt(BusinessRejectReason.FIELD, rejectReason);
        reply.setString(Text.FIELD, rejectText);
        Session.sendToTarget(reply);
        System.out.println("sendBusinessReject: " + reply.toString());  // Debugging
    }

    private void reverseRoute(Message message, Message reply) throws FieldNotFound {
        reply.getHeader().setString(SenderCompID.FIELD,
                message.getHeader().getString(TargetCompID.FIELD));
        reply.getHeader().setString(TargetCompID.FIELD,
                message.getHeader().getString(SenderCompID.FIELD));
    }

    private Message createMessage(Message message, String msgType) throws FieldNotFound {
        return messageFactory.create(message.getHeader().getString(BeginString.FIELD), msgType);
    }

    private void executionReport(Message message, SessionID sessionID) throws FieldNotFound {

        ExecID execID = (ExecID) message.getField(new ExecID());
        if (alreadyProcessed(execID, sessionID)) { return; }
        
        MessageContainer messageContainer = new MessageContainer(message);
        String orderID = messageContainer.getClOrdID();
        String orderMessage = messageContainer.getText();

        char ordStatus = message.getChar(OrdStatus.FIELD);
        if (ordStatus == OrdStatus.REJECTED) {
        	System.out.println("TraderApplication.executionReport - Rejected: " + orderMessage);
        	orderBook.orderRejected(orderID);
        }
        else {
        	orderBook.processExecutionReport(messageContainer, sessionID);
        }
        Order order = orderBook.getOrder(orderID);
        observableOrder.update(order);
        executionBook.processExecutionReport(messageContainer, orderBook.getOrder(orderID));
        if (order.getOrderBasketID()!=null)
        	observableBasket.update(orderBasketBook.getBasket(order.getOrderBasketID()));        
    }

    private void orderCancelReject(Message message, SessionID sessionID) throws FieldNotFound {
    	MessageContainer messageContainer = new MessageContainer(message);
    	orderBook.processOrderCancelReject(message.getString(ClOrdID.FIELD));
    	executionBook.processOrderCancelReject(messageContainer, orderBook.getOrder(messageContainer.getOrigClOrdID()));
    }

    private boolean alreadyProcessed(ExecID execID, SessionID sessionID) {
        HashSet<ExecID> set = execIDs.get(sessionID);
        if (set == null) {
            set = new HashSet<ExecID>();
            set.add(execID);
            execIDs.put(sessionID, set);
            return false;
        } else {
            if (set.contains(execID))
                return true;
            set.add(execID);
            return false;
        }
    }

    private void sendToBroker(Message message, SessionID sessionID) {        
    	try {
        	Session.sendToTarget(message, sessionID);
        	observableMessage.update(message);
        } catch (SessionNotFound e) {
        	System.out.println(e);
        }
    }

    public void send(Order order) {
    	orderBook.addOrder(order);
    	sendToBroker(FIXOrder.formatNewOrder(order), order.getSessionID());
    }

    public void cancel(CancelOrder cancelOrder) {
    	orderBook.addCancelOrder(cancelOrder);
    	sendToBroker(FIXOrder.formatCancelOrder(cancelOrder), cancelOrder.getSessionID());
    }

    public void replace(ReplaceOrder replaceOrder) {
    	orderBook.addReplaceOrder(replaceOrder);
        sendToBroker(FIXOrder.formatReplaceOrder(replaceOrder), replaceOrder.getSessionID());
    }

    public void cancelAllOpenOrders() {
    	for (Order o : orderBook.getAllOpenOrders()) {
            cancel(new CancelOrder(o));
    	}
    }
    
    public void sendBasket(OrderBasket orderBasket) {
    	for (Order order : orderBasket.getAllOrders()) {
	    	send(order);
    	}
    }

    // Various observable and getter functionality
    public HashSet<SessionID> getSessionIDs() {
    	return sessionIDs;
    }

    public void addLogonObserver(Observer observer) {
        observableLogon.addObserver(observer);
    }

    public void deleteLogonObserver(Observer observer) {
        observableLogon.deleteObserver(observer);
    }

    public void addOrderObserver(Observer observer) {
        observableOrder.addObserver(observer);
    }

    public void deleteOrderObserver(Observer observer) {
        observableOrder.deleteObserver(observer);
    }

    public void addMessageObserver(Observer observer) {
        observableMessage.addObserver(observer);
    }

    public void deleteMessageObserver(Observer observer) {
    	observableMessage.deleteObserver(observer);
    }

    public void addOrderBasketObserver(Observer observer) {
        observableBasket.addObserver(observer);
    }

    public void deleteOrderBasketObserver(Observer observer) {
    	observableBasket.deleteObserver(observer);
    }

    private static class ObservableMessage extends Observable {
        public void update(Message message) {
            setChanged();
            notifyObservers(message);
            clearChanged();
        }
    }
    
    private static class ObservableOrder extends Observable {
        public void update(Order order) {
            setChanged();
            notifyObservers(order);
            clearChanged();
        }
    }

    private static class ObservableLogon extends Observable {
        private final HashSet<SessionID> set = new HashSet<SessionID>();

        public void logon(SessionID sessionID) {
            set.add(sessionID);
            setChanged();
            notifyObservers(new LogonEvent(sessionID, true));
            clearChanged();
        }

        public void logoff(SessionID sessionID) {
            set.remove(sessionID);
            setChanged();
            notifyObservers(new LogonEvent(sessionID, false));
            clearChanged();
        }
    }

    private static class ObservableBasket extends Observable {
        public void update(OrderBasket orderBasket) {
            setChanged();
            notifyObservers(orderBasket);
            clearChanged();
        }
    }
 
    public void onCreate(SessionID sessionID) { }

    public void onLogon(SessionID sessionID) {
    	sessionIDs.add(sessionID);
        observableLogon.logon(sessionID);
		executionBook.setExecutionLogs(sessionID);
		// TODO following line is temporary for OrderBasket Dev
        //populateBaskets(sessionID);
    }

    public void onLogout(SessionID sessionID) {
    	sessionIDs.remove(sessionID);
    	observableLogon.logoff(sessionID);
    	executionBook.closeExecutionLogs(sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID) { }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend { }

    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound,
    	IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    	System.out.println(message.toString());
    	observableMessage.update(message);
    }


    public void populateBaskets(SessionID sessionID) {
		OrderBasket orderBasket = new OrderBasket("QUANT TRADE A");
		Order order = new Order();
		OrderSide orderSide = OrderSide.BUY;
		order.setOrderType(OrderType.LIMIT);
		order.setSymbol("GLD");
		order.setQuantity(100);
		order.setOrderSide(orderSide);
		order.setOrderTIF(OrderTIF.DAY);
		order.setLimitPrice(117.75);
		order.setOrderOpenClose(OrderOpenClose.OPEN);
		order.setSessionID(sessionID);
		orderBasket.addOrder(order);
		
		Order order2 = new Order();
		orderSide = OrderSide.BUY;
		order2.setOrderType(OrderType.LIMIT);
		order2.setSymbol("T");
		order2.setQuantity(50);
		order2.setOrderSide(orderSide);
		order2.setOrderTIF(OrderTIF.DAY);
		order2.setLimitPrice(41.99);
		order2.setOrderOpenClose(OrderOpenClose.OPEN);
		order2.setSessionID(sessionID);
		orderBasket.addOrder(order2);
		orderBasketBook.addBasket(orderBasket);
		observableBasket.update(orderBasket);

		OrderBasket orderBasket2 = new OrderBasket("QUANT TRADE B");
		Order order3 = new Order();
		orderSide = OrderSide.SELL;
		order3.setOrderType(OrderType.MARKET);
		order3.setSymbol("GOOGL");
		order3.setQuantity(400);
		order3.setLimitPrice(3.50);
		order3.setOrderSide(orderSide);
		order3.setOrderTIF(OrderTIF.DAY);
		order3.setOrderOpenClose(OrderOpenClose.OPEN);
		order3.setSessionID(sessionID);
		orderBasket2.addOrder(order3);
		
		Order order4 = new Order();
		orderSide = OrderSide.BUY;
		order4.setOrderType(OrderType.MARKET);
		order4.setSymbol("FB");
		order4.setQuantity(500);
		order4.setLimitPrice(4.50);
		order4.setOrderSide(orderSide);
		order4.setOrderTIF(OrderTIF.DAY);
		order4.setOrderOpenClose(OrderOpenClose.OPEN);
		order4.setSessionID(sessionID);
		orderBasket2.addOrder(order4);
		orderBasketBook.addBasket(orderBasket2);
		observableBasket.update(orderBasket2);
    }

}
