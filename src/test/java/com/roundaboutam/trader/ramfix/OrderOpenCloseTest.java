package com.roundaboutam.trader.ramfix;

import com.roundaboutam.trader.ramfix.OrderOpenClose;

import junit.framework.TestCase;
import quickfix.field.OpenClose;

public class OrderOpenCloseTest extends TestCase {

	public void testOrderOpenClose() {		

		assertEquals(OrderOpenClose.OPEN.toString(), "Open");
		assertEquals(OrderOpenClose.CLOSE.toString(), "Close");

		assertEquals(FIXMessage.orderOpenCloseToFIXOpenClose(OrderOpenClose.OPEN), new OpenClose(OpenClose.OPEN));
		assertEquals(FIXMessage.orderOpenCloseToFIXOpenClose(OrderOpenClose.CLOSE), new OpenClose(OpenClose.CLOSE));
		
		assertEquals(FIXMessage.FIXOpenCloseToOrderOpenClose(new OpenClose(OpenClose.OPEN)), OrderOpenClose.OPEN);
		assertEquals(FIXMessage.FIXOpenCloseToOrderOpenClose(new OpenClose(OpenClose.CLOSE)), OrderOpenClose.CLOSE);

		assertEquals(OrderOpenClose.toArray().length, 2);

	}

	public void testOrderOpenCloseFixTags() {
		
		assertEquals(FIXMessage.orderOpenCloseToFIXOpenClose(OrderOpenClose.OPEN).toString(), "77=O");
		assertEquals(FIXMessage.orderOpenCloseToFIXOpenClose(OrderOpenClose.CLOSE).toString(), "77=C");

	}

}
