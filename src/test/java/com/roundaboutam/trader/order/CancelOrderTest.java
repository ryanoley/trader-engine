package com.roundaboutam.trader.order;

import fix.OrderSide;
import fix.OrderTIF;
import fix.OrderType;
import junit.framework.TestCase;

import quickfix.SessionID;

public class CancelOrderTest extends TestCase {

	public void testCancelOrderInit() {

		Order order = new Order();
		order.setSymbol("AAPL");
		order.setOrderSide(OrderSide.BUY);
        order.setOrderType(OrderType.LIMIT);
        order.setOrderTIF(OrderTIF.DAY);
        order.setQuantity(200);
        order.setLimitPrice(100.0);        
        order.setSessionID(new SessionID("FIX.4.2", "SENDER", "SENDERSUB", "SENDERLOC",
                "TARGET", "TARGETSUB", "TARGETLOC", "QUALIFIER"));

        assertFalse(order.isCanceled());

        CancelOrder cancelOrder = new CancelOrder(order);

        assertTrue(order.isCanceled());

        assertEquals(order.getOrderID(), cancelOrder.getOrigOrderID());

	}
}
