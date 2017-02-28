package com.roundaboutam.trader.order;

import junit.framework.TestCase;

import quickfix.field.TimeInForce;
import ramfix.OrderTIF;

public class OrderTIFTest extends TestCase {

	public void testOrderTIF() {		

		assertEquals(OrderTIF.DAY.toString(), "Day");
		assertEquals(OrderTIF.IOC.toString(), "Immediate Or Cancel");
		assertEquals(OrderTIF.AT_OPEN.toString(), "At Open");
		assertEquals(OrderTIF.AT_CLOSE.toString(), "At Close");
		assertEquals(OrderTIF.GTC.toString(), "Good Till Cancelled");

		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.DAY), new TimeInForce(TimeInForce.DAY));
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.IOC), new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.AT_OPEN), new TimeInForce(TimeInForce.AT_THE_OPENING));
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.AT_CLOSE), new TimeInForce(TimeInForce.AT_THE_CLOSE));
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.GTC), new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));

		assertEquals(FIXOrder.FIXTifToOrderTif(new TimeInForce(TimeInForce.DAY)), OrderTIF.DAY);
		assertEquals(FIXOrder.FIXTifToOrderTif(new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL)), OrderTIF.IOC);
		assertEquals(FIXOrder.FIXTifToOrderTif(new TimeInForce(TimeInForce.AT_THE_OPENING)), OrderTIF.AT_OPEN);
		assertEquals(FIXOrder.FIXTifToOrderTif(new TimeInForce(TimeInForce.AT_THE_CLOSE)), OrderTIF.AT_CLOSE);
		assertEquals(FIXOrder.FIXTifToOrderTif(new TimeInForce(TimeInForce.GOOD_TILL_CANCEL)), OrderTIF.GTC);

		assertEquals(OrderTIF.toArray().length, 5);

	}
	
	public void testOrderTIFFixTags() {
		
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.DAY).toString(), "59=0");
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.GTC).toString(), "59=1");
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.IOC).toString(), "59=3");
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.AT_OPEN).toString(), "59=2");
		assertEquals(FIXOrder.orderTifToFIXTif(OrderTIF.AT_CLOSE).toString(), "59=7");

	}

}
