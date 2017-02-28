package com.roundaboutam.trader;

import junit.framework.TestCase;
import quickfix.field.ExecType;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import ramfix.ExecutionType;
import ramfix.FIXMessage;
import ramfix.MessageType;
import ramfix.OrderStatus;



public class FIXMessageTest extends TestCase {

	public void testMsgTypeMap() {
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.EXECUTION_REPORT)), MessageType.EXECUTION_REPORT);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.ORDER_CANCEL_REJECT)), MessageType.ORDER_CANCEL_REJECT);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.ORDER_SINGLE)), MessageType.NEW_ORDER);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.ORDER_CANCEL_REPLACE_REQUEST)), MessageType.REPLACE_ORDER);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.ORDER_CANCEL_REQUEST)), MessageType.CANCEL_ORDER);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.LOGON)), MessageType.LOGON);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.HEARTBEAT)), MessageType.HEARTBEAT);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.SEQUENCE_RESET)), MessageType.SEQUENCE_RESET);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.RESEND_REQUEST)), MessageType.RESEND_REQUEST);
    	assertEquals(FIXMessage.FIXMsgTypeToMessageType(new MsgType(MsgType.TEST_REQUEST)), MessageType.TEST_REQUEST);
    	
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.EXECUTION_REPORT), new MsgType(MsgType.EXECUTION_REPORT));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.ORDER_CANCEL_REJECT), new MsgType(MsgType.ORDER_CANCEL_REJECT));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.NEW_ORDER), new MsgType(MsgType.ORDER_SINGLE));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.REPLACE_ORDER), new MsgType(MsgType.ORDER_CANCEL_REPLACE_REQUEST));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.CANCEL_ORDER), new MsgType(MsgType.ORDER_CANCEL_REQUEST));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.LOGON), new MsgType(MsgType.LOGON));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.HEARTBEAT), new MsgType(MsgType.HEARTBEAT));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.SEQUENCE_RESET), new MsgType(MsgType.SEQUENCE_RESET));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.RESEND_REQUEST), new MsgType(MsgType.RESEND_REQUEST));
    	assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.TEST_REQUEST), new MsgType(MsgType.TEST_REQUEST));    	
	}

	public void testExecTypeMap() {
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.NEW)), ExecutionType.NEW);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PARTIAL_FILL)), ExecutionType.PARTIAL_FILL);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.FILL)), ExecutionType.FILL);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.DONE_FOR_DAY)), ExecutionType.DONE_FOR_DAY);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.CANCELED)), ExecutionType.CANCELED);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.REPLACE)), ExecutionType.REPLACE);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.STOPPED)), ExecutionType.STOPPED);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.SUSPENDED)), ExecutionType.SUSPENDED);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.REJECTED)), ExecutionType.REJECTED);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PENDING_CANCEL)), ExecutionType.PENDING_CANCEL);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PENDING_REPLACE)), ExecutionType.PENDING_REPLACE);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PENDING_NEW)), ExecutionType.PENDING_NEW);
    	assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.TRADE_IN_A_CLEARING_HOLD)), ExecutionType.CLEARNING_HOLD);

    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.NEW), new ExecType(ExecType.NEW));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PARTIAL_FILL), new ExecType(ExecType.PARTIAL_FILL));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.FILL), new ExecType(ExecType.FILL));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.DONE_FOR_DAY), new ExecType(ExecType.DONE_FOR_DAY));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.CANCELED), new ExecType(ExecType.CANCELED));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.REPLACE), new ExecType(ExecType.REPLACE));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.STOPPED), new ExecType(ExecType.STOPPED));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.SUSPENDED), new ExecType(ExecType.SUSPENDED));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.REJECTED), new ExecType(ExecType.REJECTED));
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_CANCEL), new ExecType(ExecType.PENDING_CANCEL));  
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_REPLACE), new ExecType(ExecType.PENDING_REPLACE)); 
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_NEW), new ExecType(ExecType.PENDING_NEW)); 
    	assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.CLEARNING_HOLD), new ExecType(ExecType.TRADE_IN_A_CLEARING_HOLD));   	
	}


	public void testOrdStatusMap() {
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.NEW)), OrderStatus.NEW);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PARTIALLY_FILLED)), OrderStatus.PARTIAL_FILL);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.FILLED)), OrderStatus.FILL);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.DONE_FOR_DAY)), OrderStatus.DONE_FOR_DAY);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.CANCELED)), OrderStatus.CANCELED);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.REPLACED)), OrderStatus.REPLACE);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.STOPPED)), OrderStatus.STOPPED);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.SUSPENDED)), OrderStatus.SUSPENDED);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.REJECTED)), OrderStatus.REJECTED);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PENDING_CANCEL)), OrderStatus.PENDING_CANCEL);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PENDING_REPLACE)), OrderStatus.PENDING_REPLACE);
    	assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PENDING_NEW)), OrderStatus.PENDING_NEW);

    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.NEW), new OrdStatus(OrdStatus.NEW));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PARTIAL_FILL), new OrdStatus(OrdStatus.PARTIALLY_FILLED));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.FILL), new OrdStatus(OrdStatus.FILLED));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.DONE_FOR_DAY), new OrdStatus(OrdStatus.DONE_FOR_DAY));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.CANCELED), new OrdStatus(OrdStatus.CANCELED));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.REPLACE), new OrdStatus(OrdStatus.REPLACED));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.STOPPED), new OrdStatus(OrdStatus.STOPPED));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.SUSPENDED), new OrdStatus(OrdStatus.SUSPENDED));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.REJECTED), new OrdStatus(OrdStatus.REJECTED));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_CANCEL), new OrdStatus(OrdStatus.PENDING_CANCEL));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_REPLACE), new OrdStatus(OrdStatus.PENDING_REPLACE));
    	assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_NEW), new OrdStatus(OrdStatus.PENDING_NEW));
	}
	


}




