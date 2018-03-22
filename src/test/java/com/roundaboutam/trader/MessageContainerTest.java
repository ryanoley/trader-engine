package com.roundaboutam.trader;


import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.ramfix.MessageType;
import com.roundaboutam.trader.ramfix.OrderStatus;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import junit.framework.TestCase;
import quickfix.InvalidMessage;
import quickfix.Message;


public class MessageContainerTest extends TestCase {

	public void testOutboundMessageContainer() throws InvalidMessage {
		String hearbeatString = "8=FIX.4.29=6435=034=17249=ROUNDPROD0152=20170228-14:55:25.75156=REALTICK10=179";
		Message heartbeatMeassage = new Message(hearbeatString);
		MessageContainer heartbeatContainer = new MessageContainer(heartbeatMeassage);
		assertEquals(heartbeatContainer.getMessageType(), MessageType.HEARTBEAT);
		assertEquals(heartbeatContainer.getMsgSeqNum(), new Integer(172));
		assertEquals(heartbeatContainer.getSenderCompID(), "ROUNDPROD01");
		assertEquals(heartbeatContainer.getTargetCompID(), "REALTICK");
		assertEquals(heartbeatContainer.getSendingTime(), "20170228-14:55:25.751");
	}

	public void testInboundMessageContainer() throws InvalidMessage {	
		String partialFillString = "8=FIX.4.29=25335=849=REALTICK56=ROUNDPROD0134=17450=ML_ALGO_US52=20170228-14:" +
				"55:0037=4c759827-59-07qm11=148829125934417=4c759827-115-0zzh-b20=0150=139=155=SPY54=138=10000" +
				"40=159=047=I32=10031=236.6300151=930014=7006=236.728660=20170228-14:55:0010=021";
		Message partialFillMsg = new Message(partialFillString);
		MessageContainer partialFillContainer = new MessageContainer(partialFillMsg);

		assertEquals(partialFillContainer.getMessageType(), MessageType.EXECUTION_REPORT);
		assertEquals(partialFillContainer.getMsgSeqNum(), new Integer(174));
		assertEquals(partialFillContainer.getSenderCompID(), "REALTICK");
		assertEquals(partialFillContainer.getTargetCompID(), "ROUNDPROD01");
		assertEquals(partialFillContainer.getSendingTime(), "20170228-14:55:00");

		assertEquals(partialFillContainer.getClOrdID(), "1488291259344");
		assertEquals(partialFillContainer.getOrderID(), "4c759827-59-07qm");
		
		assertEquals(partialFillContainer.getOrderSide(), OrderSide.BUY);
		assertEquals(partialFillContainer.getOrderStatus(), OrderStatus.PARTIAL_FILL);
		assertEquals(partialFillContainer.getExecutionType(), ExecutionType.PARTIAL_FILL);
		assertEquals(partialFillContainer.getPriceType(), PriceType.MARKET);
		assertEquals(partialFillContainer.getOrderTIF(), OrderTIF.DAY);

		assertEquals(partialFillContainer.getSymbol(), "SPY");
		assertEquals(partialFillContainer.getTransactTime(), "20170228-14:55:00");
		assertEquals(partialFillContainer.getAvgPx(), new Double(236.7286));
		assertEquals(partialFillContainer.getCumQty(), new Integer(700));
		assertEquals(partialFillContainer.getLastPx(), new Double(236.6300));
		assertEquals(partialFillContainer.getLastShares(), new Integer(100));
		assertEquals(partialFillContainer.getLeavesQty(), new Integer(9300));
		assertEquals(partialFillContainer.getOrderQty(), new Integer(10000));

		assertEquals(partialFillContainer.getDirection(), "Inbound");		

	}
	
	
	public void testCancelRequest() throws InvalidMessage {

		String cancelRequestString = "8=FIX.4.29=14435=F34=22649=ROUNDPROD0152=20180321-14:23:56.88156=REALTICK" + 
				"11=152164223688138=10041=152164221809754=155=ZVZZT60=20180321-14:23:56.88110=198";
		Message cancelRequestMsg = new Message(cancelRequestString);
		MessageContainer cancelRequestContainer = new MessageContainer(cancelRequestMsg);

		assertEquals(cancelRequestContainer.getMessageType(), MessageType.CANCEL_ORDER);
		assertEquals(cancelRequestContainer.getMsgSeqNum(), new Integer(226));
		assertEquals(cancelRequestContainer.getSenderCompID(), "ROUNDPROD01");
		assertEquals(cancelRequestContainer.getTargetCompID(), "REALTICK");
		assertEquals(cancelRequestContainer.getSendingTime(), "20180321-14:23:56.881");
		
		assertEquals(cancelRequestContainer.getClOrdID(), "1521642236881");
		assertEquals(cancelRequestContainer.getOrigClOrdID(), "1521642218097");
		
		assertEquals(cancelRequestContainer.getOrderSide(), OrderSide.BUY);
		assertEquals(cancelRequestContainer.getOrderQty(), new Integer(100));
		assertEquals(cancelRequestContainer.getSymbol(), "ZVZZT");
		assertEquals(cancelRequestContainer.getDirection(), "Outbound");	
	}

	public void testCancelResponse() throws InvalidMessage {

		String cancelResponseString = "8=FIX.4.29=29535=849=REALTICK56=ROUNDPROD0134=22750=ML_SMARTDMA" + 
				"52=20180321-14:23:5737=64959827-59-0ffn11=152164223688141=152164221809717=64959827-115-04t8-7" +
				"20=0150=439=455=ZVZZT54=138=10040=244=9.7559=047=I32=031=0.000000151=014=06=0.0000" +
				"60=20180321-14:23:5758=Cancelled by Exchange10=243";
		Message cancelResponseMsg = new Message(cancelResponseString);
		MessageContainer cancelResponseContainer = new MessageContainer(cancelResponseMsg);

		assertEquals(cancelResponseContainer.getMessageType(), MessageType.EXECUTION_REPORT);
		assertEquals(cancelResponseContainer.getMsgSeqNum(), new Integer(227));
		assertEquals(cancelResponseContainer.getSenderCompID(), "REALTICK");
		assertEquals(cancelResponseContainer.getTargetCompID(), "ROUNDPROD01");
		assertEquals(cancelResponseContainer.getSendingTime(), "20180321-14:23:57");

		assertEquals(cancelResponseContainer.getClOrdID(), "1521642236881");
		assertEquals(cancelResponseContainer.getOrigClOrdID(), "1521642218097");
		assertEquals(cancelResponseContainer.getOrderID(), "64959827-59-0ffn");
		
		assertEquals(cancelResponseContainer.getOrderStatus(), OrderStatus.CANCELED);
		assertEquals(cancelResponseContainer.getExecutionType(), ExecutionType.CANCELED);
		assertEquals(cancelResponseContainer.getOrderSide(), OrderSide.BUY);
		assertEquals(cancelResponseContainer.getPriceType(), PriceType.LIMIT);
		assertEquals(cancelResponseContainer.getOrderTIF(), OrderTIF.DAY);
		assertEquals(cancelResponseContainer.getSymbol(), "ZVZZT");
		
		assertEquals(cancelResponseContainer.getDirection(), "Inbound");	
	}		
	
	
	
}






