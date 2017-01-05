package com.roundaboutam.trader.order;

import junit.framework.TestCase;

import quickfix.field.TimeInForce;

public class OrderTIFTest extends TestCase {

	public void testOrderTIF() {		

		assertEquals(OrderTIF.DAY.toString(), "Day");
		assertEquals(OrderTIF.IOC.toString(), "Immediate Or Cancel");
		assertEquals(OrderTIF.AT_OPEN.toString(), "At Open");
		assertEquals(OrderTIF.AT_CLOSE.toString(), "At Close");
		assertEquals(OrderTIF.GTC.toString(), "Good Till Cancelled");

		assertEquals(OrderTIF.toFIX(OrderTIF.DAY), new TimeInForce(TimeInForce.DAY));
		assertEquals(OrderTIF.toFIX(OrderTIF.IOC), new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
		assertEquals(OrderTIF.toFIX(OrderTIF.AT_OPEN), new TimeInForce(TimeInForce.AT_THE_OPENING));
		assertEquals(OrderTIF.toFIX(OrderTIF.AT_CLOSE), new TimeInForce(TimeInForce.AT_THE_CLOSE));
		assertEquals(OrderTIF.toFIX(OrderTIF.GTC), new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));

		assertEquals(OrderTIF.fromFIX(new TimeInForce(TimeInForce.DAY)), OrderTIF.DAY);
		assertEquals(OrderTIF.fromFIX(new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL)), OrderTIF.IOC);
		assertEquals(OrderTIF.fromFIX(new TimeInForce(TimeInForce.AT_THE_OPENING)), OrderTIF.AT_OPEN);
		assertEquals(OrderTIF.fromFIX(new TimeInForce(TimeInForce.AT_THE_CLOSE)), OrderTIF.AT_CLOSE);
		assertEquals(OrderTIF.fromFIX(new TimeInForce(TimeInForce.GOOD_TILL_CANCEL)), OrderTIF.GTC);

		assertEquals(OrderTIF.toArray().length, 5);

	}
	
	public void testOrderTIFFixTags() {
		
		assertEquals(OrderTIF.toFIX(OrderTIF.DAY).toString(), "59=0");
		assertEquals(OrderTIF.toFIX(OrderTIF.GTC).toString(), "59=1");
		assertEquals(OrderTIF.toFIX(OrderTIF.IOC).toString(), "59=3");
		assertEquals(OrderTIF.toFIX(OrderTIF.AT_OPEN).toString(), "59=2");
		assertEquals(OrderTIF.toFIX(OrderTIF.AT_CLOSE).toString(), "59=7");

	}

}