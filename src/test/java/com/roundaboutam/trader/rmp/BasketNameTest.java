package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;



public class BasketNameTest extends TestCase {

	BasketName testBasketName = BasketName.parse("TestBasket");
		
	public void testBasketName() {		
		assertEquals(testBasketName.toString(), "TestBasket");

	}

	public void testBasketNameFixTags() {
		assertEquals(testBasketName.getRmpTag(), "6=TestBasket");
	}

}

