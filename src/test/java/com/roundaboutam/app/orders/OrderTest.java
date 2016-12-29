package com.roundaboutam.app.orders;

import junit.framework.TestCase;

public class OrderTest extends TestCase {

	public void testLimitOrder() {		
		Order lo = new Order("AAPL", OrderType.LIMIT, OrderSide.BUY, 100, 200.0);
		assertEquals(lo.getInstrumentId(), "AAPL");
		assertEquals(lo.getOrderType(), OrderType.LIMIT);
		assertEquals(lo.getOrderSide(), OrderSide.BUY);
		assertEquals(lo.getQuantity(), 100);
		assertEquals(lo.getLimitPrice(), 200.0);
		assertEquals(lo.getExecutedQuantity(), 0);
		assertEquals(lo.getAvgExecutedPrice(), -9999.0);
	}

	public void testMarketOrder() {		
		Order mo = new Order("AAPL", OrderType.MARKET, OrderSide.BUY, 100);
		assertEquals(mo.getInstrumentId(), "AAPL");
		assertEquals(mo.getOrderType(), OrderType.MARKET);
		assertEquals(mo.getOrderSide(), OrderSide.BUY);
		assertEquals(mo.getQuantity(), 100);
		assertEquals(mo.getLimitPrice(), -9999.0);
		assertEquals(mo.getExecutedQuantity(), 0);
		assertEquals(mo.getAvgExecutedPrice(), -9999.0);
	}

	public void testVwapOrder() {
		Order vo = new Order("AAPL", OrderType.VWAP, OrderSide.BUY, 100);
		assertEquals(vo.getOrderType(), OrderType.VWAP);
	}

	public void testExecution() {
		Order lo = new Order("AAPL", OrderType.LIMIT, OrderSide.BUY, 100, 200.0);
		assertEquals(lo.getExecutedQuantity(), 0);
		assertEquals(lo.getAvgExecutedPrice(), -9999.0);
		lo.updateExecutedQuantity(10, 100.0);
		assertEquals(lo.getExecutedQuantity(), 10);
		assertEquals(lo.getAvgExecutedPrice(), 100.0);
		lo.updateExecutedQuantity(10, 101.0);
		assertEquals(lo.getExecutedQuantity(), 20);
		assertEquals(lo.getAvgExecutedPrice(), 100.5);
		lo.updateExecutedQuantity(10, 102.0);
		assertEquals(lo.getExecutedQuantity(), 30);
		assertEquals(lo.getAvgExecutedPrice(), 101.0);
	}

	public void testReplace() {
		Order lo = new Order("AAPL", OrderType.LIMIT, OrderSide.BUY, 100, 200.0);
		String ordId1 = lo.getOrderId();
		lo.replaceQuantity(200);
		String ordId2 = lo.getReplaceOrderId();
		String ordId3 = lo.getOrderId();
		assertEquals(lo.getQuantity(), 200);
		assertFalse(ordId1.equals(ordId2));
		assertTrue(ordId2.equals(ordId3));
	}

}
