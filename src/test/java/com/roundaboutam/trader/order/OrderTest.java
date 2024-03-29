package com.roundaboutam.trader.order;

import junit.framework.TestCase;

public class OrderTest extends TestCase {

	public void testOrderInit() {
		Order order1 = new Order();
		Order order2 = new Order();
		assertFalse(order1.getOrderID().equals(order2.getOrderID()));
	}
	
	public void testOrderSetSymbolSuffix() {
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
	
	public void testOrderPermanentID() {
		Order order = new Order();
		assertEquals(order.getOrderID(), order.getPermanentID());
		order.setOrderID("1234");
		assertFalse(order.getOrderID().equals(order.getPermanentID()));
	}

	public void testOrderProcessFill() {
		Order order = new Order();
		order.setCumQty(200);
		int fill = order.processFill(300, 100, 50.50, 400);
		assertEquals(fill, 100);
		assertEquals(order.getCumQty(), 300);		
	}

}

