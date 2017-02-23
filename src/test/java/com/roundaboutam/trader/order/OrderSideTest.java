package com.roundaboutam.trader.order;

import junit.framework.TestCase;

import quickfix.field.Side;

public class OrderSideTest extends TestCase {

	public void testOrderSide() {		

		assertEquals(OrderSide.BUY.toString(), "Buy");
		assertEquals(OrderSide.SELL.toString(), "Sell");
		assertEquals(OrderSide.SHORT_SELL.toString(), "Short Sell");

		assertEquals(FIXOrder.sideToFIXSide(OrderSide.BUY), new Side(Side.BUY));
		assertEquals(FIXOrder.sideToFIXSide(OrderSide.SELL), new Side(Side.SELL));
		assertEquals(FIXOrder.sideToFIXSide(OrderSide.SHORT_SELL), new Side(Side.SELL_SHORT));

		assertEquals(FIXOrder.FIXSideToSide(new Side(Side.BUY)), OrderSide.BUY);
		assertEquals(FIXOrder.FIXSideToSide(new Side(Side.SELL)), OrderSide.SELL);
		assertEquals(FIXOrder.FIXSideToSide(new Side(Side.SELL_SHORT)), OrderSide.SHORT_SELL);

		assertEquals(OrderSide.toArray().length, 3);

	}

	public void testOrderSideFixTags() {
		
		assertEquals(FIXOrder.sideToFIXSide(OrderSide.BUY).toString(), "54=1");
		assertEquals(FIXOrder.sideToFIXSide(OrderSide.SELL).toString(), "54=2");
		assertEquals(FIXOrder.sideToFIXSide(OrderSide.SHORT_SELL).toString(), "54=5");

	}

}
