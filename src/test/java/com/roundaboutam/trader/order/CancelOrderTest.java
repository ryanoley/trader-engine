package com.roundaboutam.trader.order;

import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import junit.framework.TestCase;

import quickfix.SessionID;

public class CancelOrderTest extends TestCase {

	public void testCancelOrderInit() {

		Order order = new Order();
		order.setSymbol("AAPL");
		order.setOrderSide(OrderSide.BUY);
        order.setPriceType(PriceType.LIMIT);
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
