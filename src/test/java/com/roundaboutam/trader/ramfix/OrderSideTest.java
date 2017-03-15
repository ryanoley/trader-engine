package com.roundaboutam.trader.ramfix;

import com.roundaboutam.trader.rmp.OrderSide;

import junit.framework.TestCase;

import quickfix.field.Side;

public class OrderSideTest extends TestCase {

	public void testOrderSide() {		

		assertEquals(OrderSide.BUY.toString(), "Buy");
		assertEquals(OrderSide.SELL.toString(), "Sell");
		assertEquals(OrderSide.SHORT_SELL.toString(), "Short Sell");

		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.BUY), new Side(Side.BUY));
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SELL), new Side(Side.SELL));
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SHORT_SELL), new Side(Side.SELL_SHORT));

		assertEquals(FIXMessage.FIXSideToOrderSide(new Side(Side.BUY)), OrderSide.BUY);
		assertEquals(FIXMessage.FIXSideToOrderSide(new Side(Side.SELL)), OrderSide.SELL);
		assertEquals(FIXMessage.FIXSideToOrderSide(new Side(Side.SELL_SHORT)), OrderSide.SHORT_SELL);

		assertEquals(OrderSide.toArray().length, 3);

	}

	public void testOrderSideFixTags() {
		
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.BUY).toString(), "54=1");
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SELL).toString(), "54=2");
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SHORT_SELL).toString(), "54=5");

	}

}
