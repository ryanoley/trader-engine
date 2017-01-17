package com.roundaboutam.trader.order;

import junit.framework.TestCase;
import quickfix.SessionID;

public class OrderBookTest extends TestCase {

	public void testOrderBook() {
		
		OrderBook orderBook = new OrderBook();
		
		Order order = new Order();
		order.setSymbol("AAPL");
		order.setOrderSide(OrderSide.BUY);
        order.setOrderType(OrderType.LIMIT);
        order.setOrderTIF(OrderTIF.DAY);
        order.setQuantity(200);
        order.setLimitPrice(100.0);        
        order.setSessionID(new SessionID("FIX.4.2", "SENDER", "SENDERSUB", "SENDERLOC",
                "TARGET", "TARGETSUB", "TARGETLOC", "QUALIFIER"));

        orderBook.addOrder(order);

        assertEquals(order, orderBook.getOrder(order.getOrderID()));

        ReplaceOrder replaceOrder = new ReplaceOrder(order);
        replaceOrder.setLimitPrice(101.0);
        replaceOrder.setQuantity(500);

        orderBook.addReplaceOrder(replaceOrder);

        assertEquals(orderBook.getOrder(order.getOrderID()).getQuantity(), 200);
        assertEquals(orderBook.getOrder(order.getOrderID()).getLimitPrice(), 100.0);

	}

	public void testOrderBookCancelRejected() {
		
		OrderBook orderBook = new OrderBook();
		
		Order order = new Order();
		order.setSymbol("AAPL");
		order.setOrderSide(OrderSide.BUY);
        order.setOrderType(OrderType.LIMIT);
        order.setOrderTIF(OrderTIF.DAY);
        order.setQuantity(200);
        order.setLimitPrice(100.0);        
        order.setSessionID(new SessionID("FIX.4.2", "SENDER", "SENDERSUB", "SENDERLOC",
                "TARGET", "TARGETSUB", "TARGETLOC", "QUALIFIER"));

        orderBook.addOrder(order);

        assertFalse(orderBook.getOrder(order.getOrderID()).isCanceled());

        CancelOrder cancelOrder = new CancelOrder(order);

        orderBook.addCancelOrder(cancelOrder);

        orderBook.cancelRejected(cancelOrder.getOrderID());

        assertFalse(orderBook.getOrder(order.getOrderID()).isCanceled());
        assertEquals(orderBook.getOrder(order.getOrderID()).getMessage(), "Cancel Rejected");

	}

}
