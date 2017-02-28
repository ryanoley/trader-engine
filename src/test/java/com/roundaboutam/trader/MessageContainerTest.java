package com.roundaboutam.trader;


import com.roundaboutam.trader.execution.ExecutionType;
import com.roundaboutam.trader.execution.OrderStatus;
import com.roundaboutam.trader.order.OrderSide;
import com.roundaboutam.trader.order.OrderTIF;
import com.roundaboutam.trader.order.OrderType;

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
		assertEquals(heartbeatContainer.getSendingTime(), "20170228-14:55:25.751");
		assertEquals(heartbeatContainer.getTargetCompID(), "REALTICK");
	}

	public void testInboundMessageContainer() throws InvalidMessage {	
		String partialFillString = "8=FIX.4.29=25335=849=REALTICK56=ROUNDPROD0134=17450=ML_ALGO_US52=20170228-14:" +
				"55:0037=4c759827-59-07qm11=148829125934417=4c759827-115-0zzh-b20=0150=139=155=SPY54=138=10000" +
				"40=159=047=I32=10031=236.6300151=930014=7006=236.728660=20170228-14:55:0010=021";
		Message partialFillMsg = new Message(partialFillString);
		MessageContainer partialFillContainer = new MessageContainer(partialFillMsg);
	
		assertEquals(partialFillContainer.getMsgSeqNum(), new Integer(174));
		assertEquals(partialFillContainer.getSenderCompID(), "REALTICK");
		assertEquals(partialFillContainer.getSendingTime(), "20170228-14:55:00");
		assertEquals(partialFillContainer.getTargetCompID(), "ROUNDPROD01");
		
		assertEquals(partialFillContainer.getMessageType(), MessageType.EXECUTION_REPORT);
		assertEquals(partialFillContainer.getOrderSide(), OrderSide.BUY);
		assertEquals(partialFillContainer.getOrderStatus(), OrderStatus.PARTIAL_FILL);
		assertEquals(partialFillContainer.getExecutionType(), ExecutionType.PARTIAL_FILL);
		assertEquals(partialFillContainer.getOrderType(), OrderType.MARKET);
		assertEquals(partialFillContainer.getOrderTIF(), OrderTIF.DAY);

		assertEquals(partialFillContainer.getClOrdID(), "1488291259344");
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


}






