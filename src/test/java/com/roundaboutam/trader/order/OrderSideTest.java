package com.roundaboutam.trader.order;

import fix.OrderSide;
import junit.framework.TestCase;

import quickfix.field.Side;

public class OrderSideTest extends TestCase {

	public void testOrderSide() {		

		assertEquals(OrderSide.BUY.toString(), "Buy");
		assertEquals(OrderSide.SELL.toString(), "Sell");
		assertEquals(OrderSide.SHORT_SELL.toString(), "Short Sell");

		assertEquals(FIXOrder.orderSideToFIXSide(OrderSide.BUY), new Side(Side.BUY));
		assertEquals(FIXOrder.orderSideToFIXSide(OrderSide.SELL), new Side(Side.SELL));
		assertEquals(FIXOrder.orderSideToFIXSide(OrderSide.SHORT_SELL), new Side(Side.SELL_SHORT));

		assertEquals(FIXOrder.FIXSideToOrderSide(new Side(Side.BUY)), OrderSide.BUY);
		assertEquals(FIXOrder.FIXSideToOrderSide(new Side(Side.SELL)), OrderSide.SELL);
		assertEquals(FIXOrder.FIXSideToOrderSide(new Side(Side.SELL_SHORT)), OrderSide.SHORT_SELL);

		assertEquals(OrderSide.toArray().length, 3);

	}

	public void testOrderSideFixTags() {
		
		assertEquals(FIXOrder.orderSideToFIXSide(OrderSide.BUY).toString(), "54=1");
		assertEquals(FIXOrder.orderSideToFIXSide(OrderSide.SELL).toString(), "54=2");
		assertEquals(FIXOrder.orderSideToFIXSide(OrderSide.SHORT_SELL).toString(), "54=5");

	}

}
