package com.roundaboutam.trader.execution;

import com.roundaboutam.trader.FIXMessage;
import junit.framework.TestCase;
import quickfix.field.OrdStatus;



public class OrderStatusTest extends TestCase {

	public void testOrderStatus() {		
		assertEquals(OrderStatus.NEW.toString(), "New");
		assertEquals(OrderStatus.PARTIAL_FILL.toString(), "PartialFill");
		assertEquals(OrderStatus.FILL.toString(), "Filled");
		assertEquals(OrderStatus.DONE_FOR_DAY.toString(), "DoneForDay");
		assertEquals(OrderStatus.CANCELED.toString(), "Canceled");
		assertEquals(OrderStatus.REPLACE.toString(), "Replaced");
		assertEquals(OrderStatus.STOPPED.toString(), "Stopped");
		assertEquals(OrderStatus.SUSPENDED.toString(), "Suspended");
		assertEquals(OrderStatus.REJECTED.toString(), "Rejected");
		assertEquals(OrderStatus.PENDING_CANCEL.toString(), "PendingCancel");
		assertEquals(OrderStatus.PENDING_REPLACE.toString(), "PendingReplace");
		assertEquals(OrderStatus.PENDING_NEW.toString(), "PendingNew");

		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.NEW), new OrdStatus(OrdStatus.NEW));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PARTIAL_FILL), new OrdStatus(OrdStatus.PARTIALLY_FILLED));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.FILL), new OrdStatus(OrdStatus.FILLED));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.DONE_FOR_DAY), new OrdStatus(OrdStatus.DONE_FOR_DAY));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.CANCELED), new OrdStatus(OrdStatus.CANCELED));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.REPLACE), new OrdStatus(OrdStatus.REPLACED));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.STOPPED), new OrdStatus(OrdStatus.STOPPED));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.SUSPENDED), new OrdStatus(OrdStatus.SUSPENDED));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.REJECTED), new OrdStatus(OrdStatus.REJECTED));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_CANCEL), new OrdStatus(OrdStatus.PENDING_CANCEL));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_REPLACE), new OrdStatus(OrdStatus.PENDING_REPLACE));
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_NEW), new OrdStatus(OrdStatus.PENDING_NEW));
		
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.NEW)), OrderStatus.NEW);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PARTIALLY_FILLED)), OrderStatus.PARTIAL_FILL);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.FILLED)), OrderStatus.FILL);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.DONE_FOR_DAY)), OrderStatus.DONE_FOR_DAY);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.CANCELED)), OrderStatus.CANCELED);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.REPLACED)), OrderStatus.REPLACE);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.STOPPED)), OrderStatus.STOPPED);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.SUSPENDED)), OrderStatus.SUSPENDED);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.REJECTED)), OrderStatus.REJECTED);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PENDING_CANCEL)), OrderStatus.PENDING_CANCEL);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PENDING_REPLACE)), OrderStatus.PENDING_REPLACE);
		assertEquals(FIXMessage.FIXOrdStatusToOrderStatus(new OrdStatus(OrdStatus.PENDING_NEW)), OrderStatus.PENDING_NEW);

		assertEquals(OrderStatus.toArray().length, 12);
	}

	public void testOrderStatusFixTags() {

		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.NEW).toString(), "39=0");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PARTIAL_FILL).toString(), "39=1");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.FILL).toString(), "39=2");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.DONE_FOR_DAY).toString(), "39=3");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.CANCELED).toString(), "39=4");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.REPLACE).toString(), "39=5");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.STOPPED).toString(), "39=7");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.SUSPENDED).toString(), "39=9");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.REJECTED).toString(), "39=8");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_CANCEL).toString(), "39=6");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_REPLACE).toString(), "39=E");
		assertEquals(FIXMessage.orderStatusToFIXOrdStatus(OrderStatus.PENDING_NEW).toString(), "39=A");
	}

}
