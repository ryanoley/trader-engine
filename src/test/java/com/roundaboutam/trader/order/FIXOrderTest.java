package com.roundaboutam.trader.order;

import junit.framework.TestCase;
import quickfix.SessionID;


public class FIXOrderTest extends TestCase {
	
	public void testFIXOrder() {

		Order order = new Order();

		order.setSymbol("AAPL");
		order.setOrderSide(OrderSide.BUY);
        order.setOrderType(OrderType.LIMIT);
        order.setOrderTIF(OrderTIF.DAY);
        order.setQuantity(200);
        order.setLimitPrice(100.0);        
        order.setSessionID(new SessionID("FIX.4.2", "SENDER", "SENDERSUB", "SENDERLOC",
                "TARGET", "TARGETSUB", "TARGETLOC", "QUALIFIER"));

        //System.out.println(FIXOrder.formatNewOrder(order));

	}

	public void testFIXOrderVwap() {

		Order order = new VwapOrder();

		order.setSymbol("AAPL");
		order.setOrderSide(OrderSide.BUY);
		order.setOrderType(OrderType.MARKET);
        order.setOrderTIF(OrderTIF.DAY);
        order.setQuantity(200);
        order.setSessionID(new SessionID("FIX.4.2", "SENDER", "SENDERSUB", "SENDERLOC",
                "TARGET", "TARGETSUB", "TARGETLOC", "QUALIFIER"));

        //System.out.println(FIXOrder.formatNewOrder(order));

	}
	
}
