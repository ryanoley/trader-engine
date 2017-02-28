package com.roundaboutam.trader.ramfix;

import com.roundaboutam.trader.ramfix.OrderTIF;

import junit.framework.TestCase;

import quickfix.field.TimeInForce;

public class OrderTIFTest extends TestCase {

	public void testOrderTIF() {		

		assertEquals(OrderTIF.DAY.toString(), "Day");
		assertEquals(OrderTIF.IOC.toString(), "Immediate Or Cancel");
		assertEquals(OrderTIF.AT_OPEN.toString(), "At Open");
		assertEquals(OrderTIF.AT_CLOSE.toString(), "At Close");
		assertEquals(OrderTIF.GTC.toString(), "Good Till Cancelled");

		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.DAY), new TimeInForce(TimeInForce.DAY));
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.IOC), new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.AT_OPEN), new TimeInForce(TimeInForce.AT_THE_OPENING));
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.AT_CLOSE), new TimeInForce(TimeInForce.AT_THE_CLOSE));
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.GTC), new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));

		assertEquals(FIXMessage.FIXTifToOrderTif(new TimeInForce(TimeInForce.DAY)), OrderTIF.DAY);
		assertEquals(FIXMessage.FIXTifToOrderTif(new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL)), OrderTIF.IOC);
		assertEquals(FIXMessage.FIXTifToOrderTif(new TimeInForce(TimeInForce.AT_THE_OPENING)), OrderTIF.AT_OPEN);
		assertEquals(FIXMessage.FIXTifToOrderTif(new TimeInForce(TimeInForce.AT_THE_CLOSE)), OrderTIF.AT_CLOSE);
		assertEquals(FIXMessage.FIXTifToOrderTif(new TimeInForce(TimeInForce.GOOD_TILL_CANCEL)), OrderTIF.GTC);

		assertEquals(OrderTIF.toArray().length, 5);

	}
	
	public void testOrderTIFFixTags() {
		
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.DAY).toString(), "59=0");
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.GTC).toString(), "59=1");
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.IOC).toString(), "59=3");
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.AT_OPEN).toString(), "59=2");
		assertEquals(FIXMessage.orderTifToFIXTif(OrderTIF.AT_CLOSE).toString(), "59=7");

	}

}
