package com.roundaboutam.trader.order;

import junit.framework.TestCase;

import quickfix.field.OrdType;

public class OrderTypeTest extends TestCase {

	public void testOrderType() {		

		assertEquals(OrderType.MARKET.toString(), "Market");
		assertEquals(OrderType.LIMIT.toString(), "Limit");
		assertEquals(OrderType.MOC.toString(), "Market On Close");
		assertEquals(OrderType.LOC.toString(), "Limit On Close");

		assertEquals(FIXOrder.typeToFIXType(OrderType.MARKET), new OrdType(OrdType.MARKET));
		assertEquals(FIXOrder.typeToFIXType(OrderType.LIMIT), new OrdType(OrdType.LIMIT));
		assertEquals(FIXOrder.typeToFIXType(OrderType.MOC), new OrdType(OrdType.MARKET_ON_CLOSE));
		assertEquals(FIXOrder.typeToFIXType(OrderType.LOC), new OrdType(OrdType.LIMIT_ON_CLOSE));

		assertEquals(FIXOrder.FIXTypeToType(new OrdType(OrdType.MARKET)), OrderType.MARKET);
		assertEquals(FIXOrder.FIXTypeToType(new OrdType(OrdType.LIMIT)), OrderType.LIMIT);
		assertEquals(FIXOrder.FIXTypeToType(new OrdType(OrdType.MARKET_ON_CLOSE)), OrderType.MOC);
		assertEquals(FIXOrder.FIXTypeToType(new OrdType(OrdType.LIMIT_ON_CLOSE)), OrderType.LOC);

		assertEquals(OrderType.toArray().length, 4);

	}

	public void testOrderTypeFixTags() {

		assertEquals(FIXOrder.typeToFIXType(OrderType.MARKET).toString(), "40=1");
		assertEquals(FIXOrder.typeToFIXType(OrderType.LIMIT).toString(), "40=2");
		assertEquals(FIXOrder.typeToFIXType(OrderType.MOC).toString(), "40=5");
		assertEquals(FIXOrder.typeToFIXType(OrderType.LOC).toString(), "40=B");

	}

}
