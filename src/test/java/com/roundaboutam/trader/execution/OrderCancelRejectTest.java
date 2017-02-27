package com.roundaboutam.trader.execution;

import com.roundaboutam.trader.order.OrderSide;

import junit.framework.TestCase;

public class OrderCancelRejectTest extends TestCase {

	public void testOrderCancelRejectInit() {
		OrderCancelReject orderCancelReject = new OrderCancelReject(
    			"1234", "1234", "BRK", "20160101-14:45:64", 
    			OrderSide.BUY, "Reject Reason");
		orderCancelReject.setSuffix("B");
        assertEquals(orderCancelReject.getLogEntry(), "1,1234,1234,BRK/B,20160101-14:45:64,Buy,null,Reject Reason");
	}

}
