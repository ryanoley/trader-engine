package com.roundaboutam.trader;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderSide;
import com.roundaboutam.trader.order.OrderTIF;
import com.roundaboutam.trader.order.OrderType;

import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.AvgPx;
import quickfix.field.BeginString;
import quickfix.field.BusinessRejectReason;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.DeliverToCompID;
import quickfix.field.ExecID;
import quickfix.field.HandlInst;
import quickfix.field.LastPx;
import quickfix.field.LastShares;
import quickfix.field.LeavesQty;
import quickfix.field.LocateReqd;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.RefMsgType;
import quickfix.field.RefSeqNum;
import quickfix.field.SenderCompID;
import quickfix.field.SessionRejectReason;
import quickfix.field.Side;
import quickfix.field.StopPx;
import quickfix.field.Symbol;
import quickfix.field.TargetCompID;
import quickfix.field.TargetSubID;
import quickfix.field.Text;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;

import quickfix.fix42.NewOrderSingle;
import quickfix.fix42.OrderCancelRequest;
import quickfix.fix42.OrderCancelReplaceRequest;


public class TraderApplication implements Application {

	private final DefaultMessageFactory messageFactory = new DefaultMessageFactory();
    private final ObservableOrder observableOrder = new ObservableOrder();
    private final ObservableLogon observableLogon = new ObservableLogon();

    static private final TwoWayMap sideMap = new TwoWayMap();
    static private final TwoWayMap typeMap = new TwoWayMap();
    static private final TwoWayMap tifMap = new TwoWayMap();
    static private final HashMap<SessionID, HashSet<ExecID>> execIDs = 
    		new HashMap<SessionID, HashSet<ExecID>>();

	private OrderTableModel orderTableModel = null;
    private ExecutionTableModel executionTableModel = null;

	public TraderApplication(OrderTableModel orderTableModel,
            ExecutionTableModel executionTableModel) {
        this.orderTableModel = orderTableModel;
        this.executionTableModel = executionTableModel;
	}

	public void onCreate(SessionID sessionID) { }

    public void onLogon(SessionID sessionID) {
        observableLogon.logon(sessionID);
    }

    public void onLogout(SessionID sessionID) {
    	observableLogon.logoff(sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID) { }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend { }

    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound,
    	IncorrectDataFormat, IncorrectTagValue, RejectLogon { }

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
        // TODO: What does reverse route do??
        reply.getHeader().setString(SenderCompID.FIELD,
                message.getHeader().getString(TargetCompID.FIELD));
        reply.getHeader().setString(TargetCompID.FIELD,
                message.getHeader().getString(SenderCompID.FIELD));
    }

    private void executionReport(Message message, SessionID sessionID) throws FieldNotFound {

        ExecID execID = (ExecID) message.getField(new ExecID());
        if (alreadyProcessed(execID, sessionID))
        	return;

        Order order = orderTableModel.getOrder(message.getField(new ClOrdID()).getValue());
        if (order == null) {
            System.out.println("Order not found in orderTable: ");
            System.out.println(message.getField(new Symbol()).getValue());
        	return;
        }

        BigDecimal fillSize;

        if (message.isSetField(LastShares.FIELD)) {
        	LastShares lastShares = new LastShares();
            message.getField(lastShares);
            fillSize = new BigDecimal("" + lastShares.getValue());
            LeavesQty leavesQty = new LeavesQty();
            message.getField(leavesQty);

            System.out.println("LEAVES QTY: " + leavesQty);
            System.out.println("LAST SHARES: " + lastShares);
            System.out.println("FILL SIZE: " + fillSize);

        } else {
            // > FIX 4.1
        	System.out.println("HERE IN ERROR?");
            LeavesQty leavesQty = new LeavesQty();
            message.getField(leavesQty);
            fillSize = new BigDecimal(order.getQuantity()).subtract(new BigDecimal("" + leavesQty.getValue()));
        }

        if (fillSize.compareTo(BigDecimal.ZERO) > 0) {
            order.setOpen(order.getOpen() - (int) Double.parseDouble(fillSize.toPlainString()));
            order.setExecuted(Integer.parseInt(message.getString(CumQty.FIELD)));
            order.setAvgPx(Double.parseDouble(message.getString(AvgPx.FIELD)));
        }

        OrdStatus ordStatus = (OrdStatus) message.getField(new OrdStatus());
        
        if (ordStatus.valueEquals(OrdStatus.REJECTED)) {
            order.setRejected(true);
            order.setOpen(0);
        } else if (ordStatus.valueEquals(OrdStatus.CANCELED)
                || ordStatus.valueEquals(OrdStatus.DONE_FOR_DAY)) {
            order.setCanceled(true);
            order.setOpen(0);
        } else if (ordStatus.valueEquals(OrdStatus.NEW)) {
            if (order.isNew()) {
            	// TODO: This is used to indicate that the broker has received
            	// the order. Should be indicated in the table somewhere as ACK
                order.setNew(false);
            }
        } 
        
        // TODO: TESTING THESE OUT
        else if (ordStatus.valueEquals(OrdStatus.REPLACED)){
        	System.out.println("REPLACED");
        } else if (ordStatus.valueEquals(OrdStatus.PARTIALLY_FILLED)){
        	System.out.println("PARTIALLY_FILLED");
        } else if (ordStatus.valueEquals(OrdStatus.PENDING_CANCEL)){
        	System.out.println("PENDING_CANCEL");
        }

        try {
            order.setMessage(message.getField(new Text()).getValue());
        } catch (FieldNotFound e) {
        }

        orderTableModel.updateOrder(order, message.getField(new ClOrdID()).getValue());
        observableOrder.update(order);

        if (fillSize.compareTo(BigDecimal.ZERO) > 0) {
        	Execution execution = new Execution();
            execution.setExchangeID(sessionID + message.getField(new ExecID()).getValue());

            execution.setSymbol(message.getField(new Symbol()).getValue());
            execution.setQuantity(fillSize.intValue());
            if (message.isSetField(LastPx.FIELD)) {
                execution.setPrice(Double.parseDouble(message.getString(LastPx.FIELD)));
            }
            Side side = (Side) message.getField(new Side());
            execution.setSide(FIXSideToSide(side));
            executionTableModel.addExecution(execution);
        }
    }

    private void cancelReject(Message message, SessionID sessionID) throws FieldNotFound {

        String id = message.getField(new ClOrdID()).getValue();
        Order order = orderTableModel.getOrder(id);
        if (order == null)
            return;
        if (order.getOriginalID() != null)
            order = orderTableModel.getOrder(order.getOriginalID());

        try {
            order.setMessage(message.getField(new Text()).getValue());
        } catch (FieldNotFound e) {
        }
        orderTableModel.updateOrder(order, message.getField(new OrigClOrdID()).getValue());
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

    	NewOrderSingle newOrderSingle = new NewOrderSingle(
    			new ClOrdID(order.getID()), 
    			new HandlInst('1'), 
    			new Symbol(order.getSymbol()),
    			sideToFIXSide(order.getSide()), 
    			new TransactTime(), 
    			typeToFIXType(order.getType()));

    	newOrderSingle.set(new OrderQty(order.getQuantity()));

    	sendToBroker(populateOrder(order, newOrderSingle), order.getSessionID());
    }

    public Message populateOrder(Order order, Message newOrderSingle) {
    	/*
    	 * Used to add additional flags, many required by broker
    	 */
        newOrderSingle.setField(tifToFIXTif(order.getTIF()));

        if (order.getSide() == OrderSide.SHORT_SELL
                || order.getSide() == OrderSide.SHORT_SELL_EXEMPT) {
            newOrderSingle.setField(new LocateReqd(false));
            newOrderSingle.setString(5700, "BAML");
        }

        OrderType type = order.getType();

        // CUSTOM ORDERS - Always return from within brackets
        if (type == OrderType.VWAP01) {
            // Destination
        	newOrderSingle.setString(TargetSubID.FIELD, "ML_ALGO_US");
        	// Current date is automatically generated for bookends
        	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        	String currentDate = dateFormat.format(new Date());
        	// All custom tags were requested to be submitted in this format
        	String algoParams = "6401=1";
        	algoParams = algoParams + ";6403=12"; // Percent volume of 12 percent
        	algoParams = algoParams + ";6168=" + currentDate + "-14:32:00";  // Start time
        	algoParams = algoParams + ";126=" + currentDate + "-20:58:00";  // End time
        	algoParams = algoParams + ";9682=v4.3.0BRT;";
        	newOrderSingle.setString(9999, algoParams);
        	return newOrderSingle;
        }

        // STANDARD ORDERS
        else if (type == OrderType.LIMIT) {
        	newOrderSingle.setField(new Price(order.getLimit()));
        }
        else if (type == OrderType.STOP) {
            newOrderSingle.setField(new StopPx(order.getStop()));
        } 
        else if (type == OrderType.STOP_LIMIT) {
            newOrderSingle.setField(new Price(order.getLimit()));
            newOrderSingle.setField(new StopPx(order.getStop()));
        }

        // Destination
        newOrderSingle.setString(TargetSubID.FIELD, "ML_ARCA");
        return newOrderSingle;
    }
    
    public void cancel(Order order) {

    	String id = order.generateID();

        OrderCancelRequest message = new OrderCancelRequest(
                new OrigClOrdID(order.getID()), 
                new ClOrdID(id), 
                new Symbol(order.getSymbol()),
                sideToFIXSide(order.getSide()), 
                new TransactTime());

        message.setField(new OrderQty(order.getQuantity()));

        orderTableModel.addID(order, id);
        sendToBroker(message, order.getSessionID());

    }

    public void replace(Order oldOrder, Order newOrder) {

    	OrderCancelReplaceRequest message = new OrderCancelReplaceRequest(
                new OrigClOrdID(oldOrder.getID()), 
                new ClOrdID(newOrder.getID()), 
                new HandlInst('1'),
                new Symbol(oldOrder.getSymbol()), 
                sideToFIXSide(oldOrder.getSide()), 
                new TransactTime(),
                typeToFIXType(oldOrder.getType()));

    	// This doesn't seem correct either. Just maps oldOrder object to newOrder id without
    	// new params from newOrder. Do these get updated somewhere else?
        orderTableModel.addID(oldOrder, newOrder.getID());
        sendToBroker(populateCancelReplace(oldOrder, newOrder, message), oldOrder.getSessionID());
    }

    Message populateCancelReplace(Order order, Order newOrder, Message message) {
    	if (order.getType() == OrderType.LIMIT) {
    		message.setField(new Price(newOrder.getLimit()));
    	}
		message.setField(new OrderQty(newOrder.getQuantity()));
    	return message;
    }

    public Side sideToFIXSide(OrderSide side) {
        return (Side) sideMap.getFirst(side);
    }

    public OrderSide FIXSideToSide(Side side) {
        return (OrderSide) sideMap.getSecond(side);
    }

    public OrdType typeToFIXType(OrderType type) {
        return (OrdType) typeMap.getFirst(type);
    }

    public OrderType FIXTypeToType(OrdType type) {
        return (OrderType) typeMap.getSecond(type);
    }

    public TimeInForce tifToFIXTif(OrderTIF tif) {
        return (TimeInForce) tifMap.getFirst(tif);
    }

    public OrderTIF FIXTifToTif(TimeInForce tif) {
        return (OrderTIF) typeMap.getSecond(tif);
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

    static {
        sideMap.put(OrderSide.BUY, new Side(Side.BUY));
        sideMap.put(OrderSide.SELL, new Side(Side.SELL));
        sideMap.put(OrderSide.SHORT_SELL, new Side(Side.SELL_SHORT));
        sideMap.put(OrderSide.SHORT_SELL_EXEMPT, new Side(Side.SELL_SHORT_EXEMPT));
        sideMap.put(OrderSide.CROSS, new Side(Side.CROSS));
        sideMap.put(OrderSide.CROSS_SHORT, new Side(Side.CROSS_SHORT));

        typeMap.put(OrderType.MARKET, new OrdType(OrdType.MARKET));
        typeMap.put(OrderType.LIMIT, new OrdType(OrdType.LIMIT));
        typeMap.put(OrderType.STOP, new OrdType(OrdType.STOP));
        typeMap.put(OrderType.STOP_LIMIT, new OrdType(OrdType.STOP_LIMIT));
        // CUSTOM
        typeMap.put(OrderType.VWAP01, new OrdType(OrdType.MARKET));

        tifMap.put(OrderTIF.DAY, new TimeInForce(TimeInForce.DAY));
        tifMap.put(OrderTIF.IOC, new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
        tifMap.put(OrderTIF.OPG, new TimeInForce(TimeInForce.AT_THE_OPENING));
        tifMap.put(OrderTIF.GTC, new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));
        tifMap.put(OrderTIF.GTX, new TimeInForce(TimeInForce.GOOD_TILL_CROSSING));
    }

}
