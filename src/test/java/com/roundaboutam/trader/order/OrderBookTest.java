package com.roundaboutam.trader.order;

import com.roundaboutam.trader.MessageContainer;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import junit.framework.TestCase;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.SessionID;

public class OrderBookTest extends TestCase {

	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    private Order getCleanOrder() {
        Order order = new Order();
		order.setSymbol("SPY");
		order.setOrderSide(OrderSide.BUY);
        order.setPriceType(PriceType.LIMIT);
        order.setOrderTIF(OrderTIF.DAY);
        order.setQuantity(10000);
        order.setCumQty(600);
        order.setLeavesQty(9400);
        order.setLimitPrice(100.0);      
        order.setSessionID(new SessionID("FIX.4.2", "SENDER", "SENDERSUB", "SENDERLOC",
                "TARGET", "TARGETSUB", "TARGETLOC", "QUALIFIER"));
        return order;
    }
    
	public void testAddOrders() {
		
		OrderBook orderBook = new OrderBook();
		Order order1 = getCleanOrder();
		
        orderBook.addOrder(order1);
        assertEquals(order1, orderBook.getOrder(order1.getOrderID()));

        ReplaceOrder replaceOrder = new ReplaceOrder(order1);
        replaceOrder.setLimitPrice(101.0);
        replaceOrder.setQuantity(500);
        orderBook.addReplaceOrder(replaceOrder);

		order1 = getCleanOrder();
        CancelOrder cancelOrder = new CancelOrder(order1);
        orderBook.addCancelOrder(cancelOrder);
      
	}

	public void testCancelRejected() {
		
		OrderBook orderBook = new OrderBook();
		Order order1 = getCleanOrder();
		
        orderBook.addOrder(order1);
        assertFalse(orderBook.getOrder(order1.getOrderID()).isCanceled());

        CancelOrder cancelOrder = new CancelOrder(order1);
        assertTrue(orderBook.getOrder(order1.getOrderID()).isCanceled());

        orderBook.addCancelOrder(cancelOrder);
        orderBook.processOrderCancelReject(cancelOrder.getOrderID());

        assertFalse(orderBook.getOrder(order1.getOrderID()).isCanceled());
        assertEquals(orderBook.getOrder(order1.getOrderID()).getMessage(), "Cancel Rejected");

	}
	
	public void testReplaceRejected() {
		
		OrderBook orderBook = new OrderBook();
		Order order1 = getCleanOrder();
		
        orderBook.addOrder(order1);
        assertFalse(orderBook.getOrder(order1.getOrderID()).isModified());

        ReplaceOrder replaceOrder = new ReplaceOrder(order1);
        assertTrue(orderBook.getOrder(order1.getOrderID()).isModified());

        orderBook.addReplaceOrder(replaceOrder);
        orderBook.processOrderCancelReject(replaceOrder.getOrderID());

        assertFalse(orderBook.getOrder(order1.getOrderID()).isModified());
        assertEquals(orderBook.getOrder(order1.getOrderID()).getMessage(), "Replace Rejected");

	}
	
	public void testOrderRejected() {
		OrderBook orderBook = new OrderBook();
		Order order1 = getCleanOrder();
		
		orderBook.addOrder(order1);
		orderBook.orderRejected(order1.getOrderID());
		
		assertEquals(order1.getLeavesQty(), 0);
		assertTrue(order1.isRejected());
		assertEquals(order1.getMessage(), "Rejected");
	}
	
	public void testGetAllOpenOrders() {
		
		OrderBook orderBook = new OrderBook();
		Order order1 = getCleanOrder();
		Order order2 = getCleanOrder();
		
		orderBook.addOrder(order1);
		orderBook.addOrder(order2);
		
		assertEquals(orderBook.getAllOpenOrders().size(), 2);
		orderBook.orderRejected(order1.getOrderID());
		assertEquals(orderBook.getAllOpenOrders().size(), 1);
	}
	
	public void testProcessExecutionReport() throws InvalidMessage {
		
		OrderBook orderBook = new OrderBook();
		Order order1 = getCleanOrder();
		order1.setOrderID("1488291259344");
		String partialFillString = "8=FIX.4.29=25335=849=REALTICK56=ROUNDPROD0134=17450=ML_ALGO_US52=20170228-14:" +
				"55:0037=4c759827-59-07qm11=148829125934417=4c759827-115-0zzh-b20=0150=139=155=SPY54=138=10000" +
				"40=159=047=I32=10031=236.6300151=930014=7006=236.728660=20170228-14:55:0010=021";
		Message partialFillMsg = new Message(partialFillString);
		MessageContainer partialFillContainer = new MessageContainer(partialFillMsg);
		SessionID sessionID = new SessionID("FIX.4.2:ROUNDTEST02->REALTICK2:RYAN");
		
		orderBook.addOrder(order1);
		assertFalse(order1.isAcknowledged());
		assertEquals(order1.getCumQty(), 600);
		assertEquals(order1.getLeavesQty(), 9400);
		assertEquals(order1.getQuantity(), 10000);

		orderBook.processExecutionReport(partialFillContainer, sessionID);
		
		assertTrue(order1.isAcknowledged());
		assertEquals(order1.getCumQty(), 700);
		assertEquals(order1.getLeavesQty(), 9300);
		assertEquals(order1.getQuantity(), 10000);
	}
	
	
	
	
	
}














