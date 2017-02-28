package com.roundaboutam.trader.order;

import junit.framework.TestCase;

import quickfix.field.OrdType;
import ramfix.OrderType;

public class OrderTypeTest extends TestCase {

	public void testOrderType() {		

		assertEquals(OrderType.MARKET.toString(), "Market");
		assertEquals(OrderType.LIMIT.toString(), "Limit");
		assertEquals(OrderType.MOC.toString(), "Market On Close");
		assertEquals(OrderType.LOC.toString(), "Limit On Close");

		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.MARKET), new OrdType(OrdType.MARKET));
		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.LIMIT), new OrdType(OrdType.LIMIT));
		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.MOC), new OrdType(OrdType.MARKET_ON_CLOSE));
		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.LOC), new OrdType(OrdType.LIMIT_ON_CLOSE));

		assertEquals(FIXOrder.FIXTypeToOrderType(new OrdType(OrdType.MARKET)), OrderType.MARKET);
		assertEquals(FIXOrder.FIXTypeToOrderType(new OrdType(OrdType.LIMIT)), OrderType.LIMIT);
		assertEquals(FIXOrder.FIXTypeToOrderType(new OrdType(OrdType.MARKET_ON_CLOSE)), OrderType.MOC);
		assertEquals(FIXOrder.FIXTypeToOrderType(new OrdType(OrdType.LIMIT_ON_CLOSE)), OrderType.LOC);

		assertEquals(OrderType.toArray().length, 4);

	}

	public void testOrderTypeFixTags() {

		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.MARKET).toString(), "40=1");
		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.LIMIT).toString(), "40=2");
		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.MOC).toString(), "40=5");
		assertEquals(FIXOrder.orderTypeToFIXType(OrderType.LOC).toString(), "40=B");

	}

}
