package com.roundaboutam.trader.order;

import junit.framework.TestCase;

import quickfix.field.Side;

public class OrderSideTest extends TestCase {

	public void testOrderSide() {		

		assertEquals(OrderSide.BUY.toString(), "Buy");
		assertEquals(OrderSide.SELL.toString(), "Sell");
		assertEquals(OrderSide.SHORT_SELL.toString(), "Short Sell");

		assertEquals(OrderSide.toFIX(OrderSide.BUY), new Side(Side.BUY));
		assertEquals(OrderSide.toFIX(OrderSide.SELL), new Side(Side.SELL));
		assertEquals(OrderSide.toFIX(OrderSide.SHORT_SELL), new Side(Side.SELL_SHORT));

		assertEquals(OrderSide.fromFIX(new Side(Side.BUY)), OrderSide.BUY);
		assertEquals(OrderSide.fromFIX(new Side(Side.SELL)), OrderSide.SELL);
		assertEquals(OrderSide.fromFIX(new Side(Side.SELL_SHORT)), OrderSide.SHORT_SELL);

		assertEquals(OrderSide.toArray().length, 3);

	}

	public void testOrderSideFixTags() {
		
		assertEquals(OrderSide.toFIX(OrderSide.BUY).toString(), "54=1");
		assertEquals(OrderSide.toFIX(OrderSide.SELL).toString(), "54=2");
		assertEquals(OrderSide.toFIX(OrderSide.SHORT_SELL).toString(), "54=5");

	}

}
