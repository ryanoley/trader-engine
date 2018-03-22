package com.roundaboutam.trader.order;

import junit.framework.TestCase;

public class OrderBasketBookTest extends TestCase {

	private OrderBasketBook orderBasketBook;
	private OrderBasket orderBasket1;
	private OrderBasket orderBasket2;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
		orderBasketBook = new OrderBasketBook();
		orderBasket1 = new OrderBasket("BasketA");
		orderBasket2 = new OrderBasket("BasketB");

    }
    
	public void testAddDeleteBasket() {
		orderBasketBook.addBasket(orderBasket1);
		assertTrue(orderBasketBook.basketExists("BasketA"));
		assertTrue(orderBasketBook.basketExists(orderBasket1.getBasketId()));
		
		orderBasketBook.deleteBasket(orderBasket1);
		assertFalse(orderBasketBook.basketExists("BasketA"));
		assertFalse(orderBasketBook.basketExists(orderBasket1.getBasketId()));
	}

	public void testgetAllOpenBaskets() {
		orderBasketBook.addBasket(orderBasket1);
		orderBasketBook.addBasket(orderBasket2);
		assertEquals(orderBasketBook.getAllOpenBaskets().size(), 2);
	}

	
}
