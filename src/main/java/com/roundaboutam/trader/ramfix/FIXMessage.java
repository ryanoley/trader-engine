package com.roundaboutam.trader.ramfix;


import quickfix.field.ExecType;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;


public class FIXMessage {

    static private final TwoWayMap msgTypeMap = new TwoWayMap();
    static private final TwoWayMap execTypeMap = new TwoWayMap();
    static private final TwoWayMap ordStatusMap = new TwoWayMap();

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
    	
    }



}





