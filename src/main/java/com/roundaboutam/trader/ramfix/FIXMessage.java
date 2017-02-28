package com.roundaboutam.trader.ramfix;


import quickfix.field.ExecType;
import quickfix.field.MsgType;
import quickfix.field.OpenClose;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;


public class FIXMessage {

    static private final TwoWayMap msgTypeMap = new TwoWayMap();
    static private final TwoWayMap execTypeMap = new TwoWayMap();
    static private final TwoWayMap ordStatusMap = new TwoWayMap();
    static private final TwoWayMap sideMap = new TwoWayMap();
    static private final TwoWayMap tifMap = new TwoWayMap();
    static private final TwoWayMap ordTypeMap = new TwoWayMap();
    static private final TwoWayMap opencloseMap = new TwoWayMap();
    
    public static MsgType messageTypeToFIXMsgType(MessageType type) {
        return (MsgType) msgTypeMap.getFirst(type);
    }
    
    public static MessageType FIXMsgTypeToMessageType(MsgType type) {
    	checkNull(msgTypeMap.getSecond(type), type);
        return (MessageType) msgTypeMap.getSecond(type);
    }

    public static ExecType executionTypeToFIXExecType(ExecutionType type) {
        return (ExecType) execTypeMap.getFirst(type);
    }
    
    public static ExecutionType FIXExecTypeToExecutionType(ExecType type) {
    	checkNull(execTypeMap.getSecond(type), type);
        return (ExecutionType) execTypeMap.getSecond(type);
    }

    public static OrdStatus orderStatusToFIXOrdStatus(OrderStatus status) {
        return (OrdStatus) ordStatusMap.getFirst(status);
    }
    
    public static OrderStatus FIXOrdStatusToOrderStatus(OrdStatus status) {
    	checkNull(ordStatusMap.getSecond(status), status);
        return (OrderStatus) ordStatusMap.getSecond(status);
    }

    public static Side orderSideToFIXSide(OrderSide side) {
        return (Side) sideMap.getFirst(side);
    }

    public static OrderSide FIXSideToOrderSide(Side side) {
    	checkNull(sideMap.getSecond(side), side);
        return (OrderSide) sideMap.getSecond(side);
    }

    public static OrdType orderTypeToFIXOrdType(OrderType type) {
        return (OrdType) ordTypeMap.getFirst(type);
    }

    public static OrderType FIXOrdTypeToOrderType(OrdType type) {
    	checkNull(ordTypeMap.getSecond(type), type);
        return (OrderType) ordTypeMap.getSecond(type);
    }

    public static TimeInForce orderTifToFIXTif(OrderTIF tif) {
        return (TimeInForce) tifMap.getFirst(tif);
    }

    public static OrderTIF FIXTifToOrderTif(TimeInForce tif) {
    	checkNull(tifMap.getSecond(tif), tif);
        return (OrderTIF) tifMap.getSecond(tif);
    }

    public static OpenClose orderOpenCloseToFIXOpenClose(OrderOpenClose openclose) {
        return (OpenClose) opencloseMap.getFirst(openclose);
    }

    public static OrderOpenClose FIXOpenCloseToOrderOpenClose(OpenClose openclose) {
    	checkNull(opencloseMap.getSecond(openclose), openclose);
        return (OrderOpenClose) opencloseMap.getSecond(openclose);
    }
    
    private static void checkNull(Object o, Object p) {
    	if (o == null) {
    		System.out.println("Unknown FIX tag: " + p.toString());
    	}
    }

    static {
    	msgTypeMap.put(MessageType.EXECUTION_REPORT, new MsgType(MsgType.EXECUTION_REPORT));
    	msgTypeMap.put(MessageType.ORDER_CANCEL_REJECT, new MsgType(MsgType.ORDER_CANCEL_REJECT));
    	msgTypeMap.put(MessageType.NEW_ORDER, new MsgType(MsgType.ORDER_SINGLE));
    	msgTypeMap.put(MessageType.REPLACE_ORDER, new MsgType(MsgType.ORDER_CANCEL_REPLACE_REQUEST));
    	msgTypeMap.put(MessageType.CANCEL_ORDER, new MsgType(MsgType.ORDER_CANCEL_REQUEST));
    	msgTypeMap.put(MessageType.LOGON, new MsgType(MsgType.LOGON));
    	msgTypeMap.put(MessageType.HEARTBEAT, new MsgType(MsgType.HEARTBEAT));
    	msgTypeMap.put(MessageType.SEQUENCE_RESET, new MsgType(MsgType.SEQUENCE_RESET));
    	msgTypeMap.put(MessageType.RESEND_REQUEST, new MsgType(MsgType.RESEND_REQUEST));
    	msgTypeMap.put(MessageType.TEST_REQUEST, new MsgType(MsgType.TEST_REQUEST));
    	
    	execTypeMap.put(ExecutionType.NEW, new ExecType(ExecType.NEW));
    	execTypeMap.put(ExecutionType.PARTIAL_FILL, new ExecType(ExecType.PARTIAL_FILL));
    	execTypeMap.put(ExecutionType.FILL, new ExecType(ExecType.FILL));
    	execTypeMap.put(ExecutionType.DONE_FOR_DAY, new ExecType(ExecType.DONE_FOR_DAY));
    	execTypeMap.put(ExecutionType.CANCELED, new ExecType(ExecType.CANCELED));
    	execTypeMap.put(ExecutionType.REPLACE, new ExecType(ExecType.REPLACE));
    	execTypeMap.put(ExecutionType.STOPPED, new ExecType(ExecType.STOPPED));
    	execTypeMap.put(ExecutionType.SUSPENDED, new ExecType(ExecType.SUSPENDED));
    	execTypeMap.put(ExecutionType.REJECTED, new ExecType(ExecType.REJECTED));
    	execTypeMap.put(ExecutionType.PENDING_CANCEL, new ExecType(ExecType.PENDING_CANCEL));
    	execTypeMap.put(ExecutionType.PENDING_REPLACE, new ExecType(ExecType.PENDING_REPLACE));
    	execTypeMap.put(ExecutionType.PENDING_NEW, new ExecType(ExecType.PENDING_NEW));
    	execTypeMap.put(ExecutionType.CLEARNING_HOLD, new ExecType(ExecType.TRADE_IN_A_CLEARING_HOLD));
    	
    	ordStatusMap.put(OrderStatus.NEW, new OrdStatus(OrdStatus.NEW));
    	ordStatusMap.put(OrderStatus.PARTIAL_FILL, new OrdStatus(OrdStatus.PARTIALLY_FILLED));
    	ordStatusMap.put(OrderStatus.FILL, new OrdStatus(OrdStatus.FILLED));
    	ordStatusMap.put(OrderStatus.DONE_FOR_DAY, new OrdStatus(OrdStatus.DONE_FOR_DAY));
    	ordStatusMap.put(OrderStatus.CANCELED, new OrdStatus(OrdStatus.CANCELED));
    	ordStatusMap.put(OrderStatus.REPLACE, new OrdStatus(OrdStatus.REPLACED));
    	ordStatusMap.put(OrderStatus.STOPPED, new OrdStatus(OrdStatus.STOPPED));
    	ordStatusMap.put(OrderStatus.SUSPENDED, new OrdStatus(OrdStatus.SUSPENDED));
    	ordStatusMap.put(OrderStatus.REJECTED, new OrdStatus(OrdStatus.REJECTED));
    	ordStatusMap.put(OrderStatus.PENDING_CANCEL, new OrdStatus(OrdStatus.PENDING_CANCEL));
    	ordStatusMap.put(OrderStatus.PENDING_REPLACE, new OrdStatus(OrdStatus.PENDING_REPLACE));
    	ordStatusMap.put(OrderStatus.PENDING_NEW, new OrdStatus(OrdStatus.PENDING_NEW));

    	sideMap.put(OrderSide.BUY, new Side(Side.BUY));
    	sideMap.put(OrderSide.SELL, new Side(Side.SELL));
    	sideMap.put(OrderSide.SHORT_SELL, new Side(Side.SELL_SHORT));

    	ordTypeMap.put(OrderType.MARKET, new OrdType(OrdType.MARKET));
    	ordTypeMap.put(OrderType.LIMIT, new OrdType(OrdType.LIMIT));
    	ordTypeMap.put(OrderType.MOC, new OrdType(OrdType.MARKET_ON_CLOSE));
    	ordTypeMap.put(OrderType.LOC, new OrdType(OrdType.LIMIT_ON_CLOSE));

	    tifMap.put(OrderTIF.DAY, new TimeInForce(TimeInForce.DAY));
	    tifMap.put(OrderTIF.IOC, new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
	    tifMap.put(OrderTIF.AT_OPEN, new TimeInForce(TimeInForce.AT_THE_OPENING));
	    tifMap.put(OrderTIF.AT_CLOSE, new TimeInForce(TimeInForce.AT_THE_CLOSE));
	    tifMap.put(OrderTIF.GTC, new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));
	    
    	opencloseMap.put(OrderOpenClose.OPEN, new OpenClose(OpenClose.OPEN));
    	opencloseMap.put(OrderOpenClose.CLOSE,  new OpenClose(OpenClose.CLOSE));	
    }


    public static TwoWayMap getMsgTypeMap() {
    	return msgTypeMap;
    }
    public static TwoWayMap getExecTypeMap() {
    	return execTypeMap;
    }
    public static TwoWayMap getOrdStatusMap() {
    	return ordStatusMap;
    }
    public static TwoWayMap getSideMap() {
    	return sideMap;
    }
    public static TwoWayMap getOrdTypeMap() {
    	return ordTypeMap;
    }
    public static TwoWayMap getTifMap() {
    	return tifMap;
    }
    public static TwoWayMap getOpenCloseMap() {
    	return opencloseMap;
    }

}





