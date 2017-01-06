package com.roundaboutam.trader.order;

import java.util.HashMap;

import junit.framework.TestCase;

public class OrderTest extends TestCase {

	public void testOrderInit() {
		Order order1 = new Order();
		Order order2 = new Order();
		assertFalse(order1.getOrderID().equals(order2.getOrderID()));
	}
	
	public void testOrderSetSymbolParse() {
		Order order = new Order();
		order.setSymbol("BRK.B");
		assertEquals(order.getSymbol(), "BRK");
		assertEquals(order.getSuffix(), "B");
		order.setSymbol("BRK/B");
		assertEquals(order.getSymbol(), "BRK");
		assertEquals(order.getSuffix(), "B");
		order.setSymbol("BRK B");
		assertEquals(order.getSymbol(), "BRK");
		assertEquals(order.getSuffix(), "B");
	}
	
	public void testBaseClassHashMap() {
        HashMap<Integer, BaseOrder> orderMap = new HashMap<Integer, BaseOrder>();
        Order order = new Order();
        ReplaceOrder replaceOrder = new ReplaceOrder(order);
        orderMap.put(1, order);
        orderMap.put(2,  replaceOrder);
        assertEquals(orderMap.size(), 2);
	}

}
