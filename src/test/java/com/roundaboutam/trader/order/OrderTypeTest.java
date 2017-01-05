package com.roundaboutam.trader.order;

import junit.framework.TestCase;

import quickfix.field.OrdType;

public class OrderTypeTest extends TestCase {

	public void testOrderType() {		

		assertEquals(OrderType.MARKET.toString(), "Market");
		assertEquals(OrderType.LIMIT.toString(), "Limit");
		assertEquals(OrderType.MOC.toString(), "Market On Close");
		assertEquals(OrderType.LOC.toString(), "Limit On Close");

		assertEquals(OrderType.toFIX(OrderType.MARKET), new OrdType(OrdType.MARKET));
		assertEquals(OrderType.toFIX(OrderType.LIMIT), new OrdType(OrdType.LIMIT));
		assertEquals(OrderType.toFIX(OrderType.MOC), new OrdType(OrdType.MARKET_ON_CLOSE));
		assertEquals(OrderType.toFIX(OrderType.LOC), new OrdType(OrdType.LIMIT_ON_CLOSE));

		assertEquals(OrderType.fromFIX(new OrdType(OrdType.MARKET)), OrderType.MARKET);
		assertEquals(OrderType.fromFIX(new OrdType(OrdType.LIMIT)), OrderType.LIMIT);
		assertEquals(OrderType.fromFIX(new OrdType(OrdType.MARKET_ON_CLOSE)), OrderType.MOC);
		assertEquals(OrderType.fromFIX(new OrdType(OrdType.LIMIT_ON_CLOSE)), OrderType.LOC);

		assertEquals(OrderType.toArray().length, 4);

	}

	public void testOrderTypeFixTags() {

		assertEquals(OrderType.toFIX(OrderType.MARKET).toString(), "40=1");
		assertEquals(OrderType.toFIX(OrderType.LIMIT).toString(), "40=2");
		assertEquals(OrderType.toFIX(OrderType.MOC).toString(), "40=5");
		assertEquals(OrderType.toFIX(OrderType.LOC).toString(), "40=B");

	}

}
