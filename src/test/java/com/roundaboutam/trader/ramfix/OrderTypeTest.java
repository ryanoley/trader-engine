package com.roundaboutam.trader.ramfix;

import com.roundaboutam.trader.ramfix.OrderType;

import junit.framework.TestCase;

import quickfix.field.OrdType;

public class OrderTypeTest extends TestCase {

	public void testOrderType() {		

		assertEquals(OrderType.MARKET.toString(), "Market");
		assertEquals(OrderType.LIMIT.toString(), "Limit");
		assertEquals(OrderType.MOC.toString(), "Market On Close");
		assertEquals(OrderType.LOC.toString(), "Limit On Close");

		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.MARKET), new OrdType(OrdType.MARKET));
		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.LIMIT), new OrdType(OrdType.LIMIT));
		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.MOC), new OrdType(OrdType.MARKET_ON_CLOSE));
		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.LOC), new OrdType(OrdType.LIMIT_ON_CLOSE));

		assertEquals(FIXMessage.FIXOrdTypeToOrderType(new OrdType(OrdType.MARKET)), OrderType.MARKET);
		assertEquals(FIXMessage.FIXOrdTypeToOrderType(new OrdType(OrdType.LIMIT)), OrderType.LIMIT);
		assertEquals(FIXMessage.FIXOrdTypeToOrderType(new OrdType(OrdType.MARKET_ON_CLOSE)), OrderType.MOC);
		assertEquals(FIXMessage.FIXOrdTypeToOrderType(new OrdType(OrdType.LIMIT_ON_CLOSE)), OrderType.LOC);

		assertEquals(OrderType.toArray().length, 4);

	}

	public void testOrderTypeFixTags() {

		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.MARKET).toString(), "40=1");
		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.LIMIT).toString(), "40=2");
		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.MOC).toString(), "40=5");
		assertEquals(FIXMessage.orderTypeToFIXOrdType(OrderType.LOC).toString(), "40=B");

	}

}
