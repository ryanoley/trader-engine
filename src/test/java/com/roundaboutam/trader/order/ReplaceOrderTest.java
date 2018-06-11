package com.roundaboutam.trader.order;

import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import junit.framework.TestCase;

import quickfix.SessionID;

public class ReplaceOrderTest extends TestCase {


	public void testReplaceOrderInit() {

		Order order = new Order();
		order.setSymbol("AAPL");
		order.setOrderSide(OrderSide.BUY);
        order.setPriceType(PriceType.LIMIT);
        order.setOrderTIF(OrderTIF.DAY);
        order.setQuantity(200);
        order.setLimitPrice(100.0);        
        order.setSessionID(new SessionID("FIX.4.2", "SENDER", "SENDERSUB", "SENDERLOC",
                "TARGET", "TARGETSUB", "TARGETLOC", "QUALIFIER"));

        assertFalse(order.isModified());

        ReplaceOrder replaceOrder = new ReplaceOrder(order);

        assertTrue(order.isModified());
        assertEquals(order.getOrderID(), replaceOrder.getOrigOrderID());
        assertEquals(order.getQuantity(), replaceOrder.getQuantity());
        assertEquals(order.getLimitPrice(), replaceOrder.getLimitPrice());

        replaceOrder.setLimitPrice(101.0);
        replaceOrder.setQuantity(500);
        assertFalse(replaceOrder.getLimitPrice().equals(order.getLimitPrice()));
        assertFalse(replaceOrder.getQuantity() == order.getQuantity());

	}

}
