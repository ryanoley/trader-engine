package com.roundaboutam.trader;

import junit.framework.TestCase;
import quickfix.field.MsgType;
import ramfix.FIXMessage;
import ramfix.MessageType;



public class MessageTypeTest extends TestCase {

	public void testMessageType() {		
		assertEquals(MessageType.EXECUTION_REPORT.toString(), "ExecutionReport");
		assertEquals(MessageType.ORDER_CANCEL_REJECT.toString(), "Reject");
		assertEquals(MessageType.NEW_ORDER.toString(), "NewOrder");
		assertEquals(MessageType.REPLACE_ORDER.toString(), "ReplaceOrder");
		assertEquals(MessageType.CANCEL_ORDER.toString(), "CancelOrder");
		assertEquals(MessageType.LOGON.toString(), "Logon");
		assertEquals(MessageType.HEARTBEAT.toString(), "Heartbeat");
		assertEquals(MessageType.SEQUENCE_RESET.toString(), "SequenceReset");
		assertEquals(MessageType.RESEND_REQUEST.toString(), "ResendRequest");
		assertEquals(MessageType.TEST_REQUEST.toString(), "TestRequest");


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

		assertEquals(MessageType.toArray().length, 10);
	}

	public void testMessageTypeFixTags() {

		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.EXECUTION_REPORT).toString(), "35=8");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.ORDER_CANCEL_REJECT).toString(), "35=9");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.NEW_ORDER).toString(), "35=D");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.REPLACE_ORDER).toString(), "35=G");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.CANCEL_ORDER).toString(), "35=F");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.LOGON).toString(), "35=A");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.HEARTBEAT).toString(), "35=0");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.SEQUENCE_RESET).toString(), "35=4");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.RESEND_REQUEST).toString(), "35=2");
		assertEquals(FIXMessage.messageTypeToFIXMsgType(MessageType.TEST_REQUEST).toString(), "35=1");

	}

}
