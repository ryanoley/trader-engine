package com.roundaboutam.trader.rmp;

import com.roundaboutam.trader.ramfix.FIXMessage;
import com.roundaboutam.trader.rmp.OrderSide;

import junit.framework.TestCase;

import quickfix.field.Side;

public class OrderSideTest extends TestCase {

	public void testOrderSide() {		

		assertEquals(OrderSide.BUY.toString(), "BY");
		assertEquals(OrderSide.SELL.toString(), "SL");
		assertEquals(OrderSide.SHORT_SELL.toString(), "SS");
		assertEquals(OrderSide.BUY_TO_COVER.toString(), "BTC");

		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.BUY), new Side(Side.BUY));
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SELL), new Side(Side.SELL));
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SHORT_SELL), new Side(Side.SELL_SHORT));

		assertEquals(FIXMessage.FIXSideToOrderSide(new Side(Side.BUY)), OrderSide.BUY);
		assertEquals(FIXMessage.FIXSideToOrderSide(new Side(Side.SELL)), OrderSide.SELL);
		assertEquals(FIXMessage.FIXSideToOrderSide(new Side(Side.SELL_SHORT)), OrderSide.SHORT_SELL);

		assertEquals(OrderSide.toArray().length, 4);
	}

	public void testOrderSideFixTags() {
		
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.BUY).toString(), "54=1");
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SELL).toString(), "54=2");
		assertEquals(FIXMessage.orderSideToFIXSide(OrderSide.SHORT_SELL).toString(), "54=5");
	}
	
	public void testOrderSideRMPTags() {
		assertEquals(OrderSide.BUY.getRmpTag(), OrderSide.RMPFieldID + "=BY");
		assertEquals(OrderSide.SELL.getRmpTag(), OrderSide.RMPFieldID + "=SL");
		assertEquals(OrderSide.SHORT_SELL.getRmpTag(), OrderSide.RMPFieldID + "=SS");
		assertEquals(OrderSide.BUY_TO_COVER.getRmpTag(), OrderSide.RMPFieldID + "=BTC");
	}

}
