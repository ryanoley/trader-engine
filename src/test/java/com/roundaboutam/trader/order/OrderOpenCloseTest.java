package com.roundaboutam.trader.order;

import junit.framework.TestCase;
import quickfix.field.OpenClose;
import ramfix.OrderOpenClose;

public class OrderOpenCloseTest extends TestCase {

	public void testOrderOpenClose() {		

		assertEquals(OrderOpenClose.OPEN.toString(), "Open");
		assertEquals(OrderOpenClose.CLOSE.toString(), "Close");

		assertEquals(FIXOrder.orderOpenCloseToFIXOpenClose(OrderOpenClose.OPEN), new OpenClose(OpenClose.OPEN));
		assertEquals(FIXOrder.orderOpenCloseToFIXOpenClose(OrderOpenClose.CLOSE), new OpenClose(OpenClose.CLOSE));
		
		assertEquals(FIXOrder.FIXOpenCloseToOrderOpenClose(new OpenClose(OpenClose.OPEN)), OrderOpenClose.OPEN);
		assertEquals(FIXOrder.FIXOpenCloseToOrderOpenClose(new OpenClose(OpenClose.CLOSE)), OrderOpenClose.CLOSE);

		assertEquals(OrderOpenClose.toArray().length, 2);

	}

	public void testOrderOpenCloseFixTags() {
		
		assertEquals(FIXOrder.orderOpenCloseToFIXOpenClose(OrderOpenClose.OPEN).toString(), "77=O");
		assertEquals(FIXOrder.orderOpenCloseToFIXOpenClose(OrderOpenClose.CLOSE).toString(), "77=C");

	}

}
