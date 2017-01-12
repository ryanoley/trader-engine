package com.roundaboutam.trader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderBook;
import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.ReplaceOrder;
import com.roundaboutam.trader.order.FIXOrder;
import com.roundaboutam.trader.order.OrderSide;
import com.roundaboutam.trader.order.OrderType;
import com.roundaboutam.trader.execution.Execution;
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
import quickfix.field.AvgPx;
import quickfix.field.BeginString;
import quickfix.field.BusinessRejectReason;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.DeliverToCompID;
import quickfix.field.ExecID;
import quickfix.field.LastPx;
import quickfix.field.LeavesQty;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.RefMsgType;
import quickfix.field.RefSeqNum;
import quickfix.field.SenderCompID;
import quickfix.field.SessionRejectReason;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.SymbolSfx;
import quickfix.field.TargetCompID;
import quickfix.field.Text;
import quickfix.field.TransactTime;

public class TraderApplication implements Application {

    private final ObservableOrder observableOrder = new ObservableOrder();
    private final ObservableLogon observableLogon = new ObservableLogon();

    private final OrderBook orderBook;
	private final ExecutionBook executionBook;

    private final DefaultMessageFactory messageFactory = new DefaultMessageFactory();

	static private final HashMap<SessionID, HashSet<ExecID>> execIDs =  
			new HashMap<SessionID, HashSet<ExecID>>();

	static private final HashSet<SessionID> sessionIDs = new HashSet<SessionID>();

	public TraderApplication(SessionSettings settings) throws ConfigError, FieldConvertError {
		orderBook = new OrderBook();
		executionBook = new ExecutionBook(settings.getString("CustomLogPath"));
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
                if (message.getHeader().isSetField(DeliverToCompID.FIELD)) {
                	sendSessionReject(message, SessionRejectReason.COMPID_PROBLEM);
                } else if (message.getHeader().getField(msgType).valueEquals("8")) {
                	executionReport(message, sessionID);
                } else if (message.getHeader().getField(msgType).valueEquals("9")) {
                	cancelReject(message, sessionID);
                } else {
                	sendBusinessReject(message, BusinessRejectReason.UNSUPPORTED_MESSAGE_TYPE,
                			"Unsupported Message Type");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendSessionReject(Message message, int rejectReason) throws FieldNotFound,
    		SessionNotFound {
    	Message reply = createMessage(message, MsgType.REJECT);
    	reverseRoute(message, reply);
    	String refSeqNum = message.getHeader().getString(MsgSeqNum.FIELD);
    	reply.setString(RefSeqNum.FIELD, refSeqNum);
    	reply.setString(RefMsgType.FIELD, message.getHeader().getString(MsgType.FIELD));
    	reply.setInt(SessionRejectReason.FIELD, rejectReason);
    	Session.sendToTarget(reply);
    	System.out.println("sendSessionReject: " + reply.toString());  // Debugging
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

    private Message createMessage(Message message, String msgType) throws FieldNotFound {
        return messageFactory.create(message.getHeader().getString(BeginString.FIELD), msgType);
    }

    private void reverseRoute(Message message, Message reply) throws FieldNotFound {
        reply.getHeader().setString(SenderCompID.FIELD,
                message.getHeader().getString(TargetCompID.FIELD));
        reply.getHeader().setString(TargetCompID.FIELD,
                message.getHeader().getString(SenderCompID.FIELD));
    }

    private void executionReport(Message message, SessionID sessionID) throws FieldNotFound {

        ExecID execID = (ExecID) message.getField(new ExecID());
        if (alreadyProcessed(execID, sessionID)) { return; }

        String orderID = message.getString(ClOrdID.FIELD);

        OrdStatus ordStatus = (OrdStatus) message.getField(new OrdStatus());        
        if (ordStatus.valueEquals(OrdStatus.REJECTED)) {
        	System.out.println("TraderApplication.executionReport: Rejected");
        	// 1. Rejects come here when there are errors in order. Replicate with BRK/B order. Wrong suffix
        	// 2. Also, I am getting here when cancelling Unacknowledged orders. The broker message is: Cancelled by Exchange
        	// which seems like a normal message. Is the OrdStatus then just from the original order?
        	orderBook.orderRejected(orderID);
        	return;
        } else if (ordStatus.valueEquals(OrdStatus.CANCELED) 
        		|| ordStatus.valueEquals(OrdStatus.DONE_FOR_DAY)) {
        	// Seems like rejects coming here as well. Replicate with large market orders
        	// Cancels generated by me come here as well
        	System.out.println("TraderApplication.executionReport: Canceled");
        }

        String orderMessage = null;
        try {
        	orderMessage = message.getString(Text.FIELD);
        	System.out.println(orderMessage);
        } catch (Exception e) {
        }

        int fillSize = orderBook.processExecutionReport(
        		orderID,
        		message.getString(Symbol.FIELD),
        		Integer.parseInt(message.getString(OrderQty.FIELD)), 
        		Integer.parseInt(message.getString(CumQty.FIELD)), 
        		Integer.parseInt(message.getString(LeavesQty.FIELD)), 
        		Double.parseDouble(message.getString(AvgPx.FIELD)),
        		OrderSide.fromFIX((Side) message.getField(new Side())),
        		OrderType.fromFIX((OrdType) message.getField(new OrdType())),
        		sessionID, orderMessage);

        observableOrder.update(orderBook.getOrder(orderID));

        if (fillSize > 0) {
        	Execution execution = new Execution(
        			orderID,
        			orderBook.getOrder(orderID).getPermanentID(),
        			message.getString(Symbol.FIELD),
        			message.getString(TransactTime.FIELD),
        			message.getString(ExecID.FIELD),
        			OrderSide.fromFIX((Side) message.getField(new Side())),
        			fillSize,
        			Double.parseDouble(message.getString(LastPx.FIELD))
        			);

            if (message.isSetField(SymbolSfx.FIELD)) {
                execution.setSuffix(message.getString(SymbolSfx.FIELD));
            }
            execution.setCustomTag(orderBook.getOrder(orderID).getCustomTag());
            // TODO: Market data used here to capture BidAsk
            //execution.setBid(0);
            //execution.setAsk(0);
            executionBook.addExecution(execution);
        }
    }

    private void cancelReject(Message message, SessionID sessionID) throws FieldNotFound {
    	orderBook.cancelRejected(message.getString(ClOrdID.FIELD));
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
        } catch (SessionNotFound e) {
        	System.out.println(e);
        }
    }

    public void send(Order order) {
    	orderBook.addOrder(order);
    	observableOrder.update(order);
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
    		sendToBroker(FIXOrder.formatCancelOrder(new CancelOrder(o)), o.getSessionID());
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

    public void onCreate(SessionID sessionID) { }

    public void onLogon(SessionID sessionID) {
    	sessionIDs.add(sessionID);
        observableLogon.logon(sessionID);
    }

    public void onLogout(SessionID sessionID) {
    	sessionIDs.remove(sessionID);
    	observableLogon.logoff(sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID) { }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend { }

    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound,
    	IncorrectDataFormat, IncorrectTagValue, RejectLogon { }

}
