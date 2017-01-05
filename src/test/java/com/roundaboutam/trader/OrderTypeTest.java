package com.roundaboutam.trader;

import com.roundaboutam.trader.order.OrderType;

import junit.framework.TestCase;

public class OrderTypeTest extends TestCase {

	public void testOrderTypeFunctionality() {		

		OrderType vwap01 = OrderType.VWAP01;
		assertEquals(vwap01.toString(), "VWAP01");
		assertEquals(vwap01.getName(), "VWAP01");

	}

}
