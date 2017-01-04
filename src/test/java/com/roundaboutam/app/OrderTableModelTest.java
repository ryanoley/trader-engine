package com.roundaboutam.app;

import junit.framework.TestCase;

public class OrderTableModelTest extends TestCase {

	public void testOrderTableModelAddReplaceOrder() {		

		OrderTableModel orderTable = new OrderTableModel();
		// Create an order to go in
		Order order = new Order();
        order.setSide(OrderSide.BUY);
        order.setType(OrderType.LIMIT);
        order.setTIF(OrderTIF.DAY);
        order.setSymbol("IBM");
        order.setQuantity(1234);
        order.setOpen(order.getQuantity());
		orderTable.addOrder(order);
 
		// Asserts
		assertEquals(orderTable.getOrder(0).getSymbol(), "IBM");
		assertEquals(orderTable.getOrder(order.getID()).getSymbol(), "IBM");
	}

}