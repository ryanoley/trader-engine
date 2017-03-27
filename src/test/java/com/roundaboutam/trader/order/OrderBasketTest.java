package com.roundaboutam.trader.order;

import junit.framework.TestCase;

public class OrderBasketTest extends TestCase {

	public void testOrderBasketInit() {
		OrderBasket orderBasket1 = new OrderBasket("BasketA");
		OrderBasket orderBasket2 = new OrderBasket("BasketB");
		assertFalse(orderBasket1.getBasketId().equals(orderBasket2.getBasketId()));
	}
	
	public void testAddRemoveOrders() {
		OrderBasket orderBasket = new OrderBasket("BasketA");
		Order order1 = new Order();
		Order order2 = new Order();
		orderBasket.addOrder(order1);
		orderBasket.addOrder(order2);
		assertEquals(orderBasket.getAllOpenOrders().size(), 2);
		orderBasket.removeOrder(order1);
		assertEquals(orderBasket.getAllOpenOrders().size(), 1);
	}

}
