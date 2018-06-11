package com.roundaboutam.trader.order;

import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.rmp.OrderSide;

import junit.framework.TestCase;

public class OrderBasketTest extends TestCase {

	private Order order1;
	private Order order2;
	private OrderBasket orderBasket;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
		order1 = new Order();
        order1.setSymbol("IBM");
        order1.setQuantity(500);
        order1.setCumQty(300);
        order1.setLeavesQty(200);
        order1.setOrderSide(OrderSide.BUY);
        order1.setOrderOpenClose(OrderOpenClose.CLOSE);
        order1.setAvgPx(100);

		order2 = new Order();
		order2.setSymbol("AAPL");
		order2.setQuantity(300);
		order2.setCumQty(300);
		order2.setLeavesQty(0);
        order1.setOrderOpenClose(OrderOpenClose.CLOSE);
		order2.setOrderSide(OrderSide.SELL);
		order2.setAvgPx(50);
		
		orderBasket = new OrderBasket("BasketA");
		orderBasket.addOrder(order1);
		orderBasket.addOrder(order2);
    }
   
	public void testOrderBasketInit() {
		OrderBasket orderBasket1 = new OrderBasket("BasketA");
		OrderBasket orderBasket2 = new OrderBasket("BasketB");
		assertFalse(orderBasket1.getBasketId().equals(orderBasket2.getBasketId()));
	}

	public void testAddRemoveOrders() {
		OrderBasket orderBasket1 = new OrderBasket("BasketA");
		orderBasket1.addOrder(order1);
		orderBasket1.addOrder(order2);
		assertEquals(orderBasket1.getAllOpenOrders().size(), 1);
		assertEquals(orderBasket1.getAllOrders().size(), 2);
		orderBasket1.removeOrder(order1);
		assertEquals(orderBasket1.getAllOpenOrders().size(), 0);
		assertEquals(orderBasket1.getAllOrders().size(), 1);
	}

	public void testGetSummary() {
		orderBasket.getSummary();
		
		assertEquals(orderBasket.nOrdersBY, 0);
		assertEquals(orderBasket.nOrdersBTC, 1);
		assertEquals(orderBasket.nOrdersSL, 1);
		assertEquals(orderBasket.nOrdersSS, 0);

		assertEquals(orderBasket.nSharesBY, 0);
		assertEquals(orderBasket.nSharesBTC, 500);
		assertEquals(orderBasket.nSharesSL, 300);
		assertEquals(orderBasket.nSharesSS, 0);

		assertEquals(orderBasket.totalDollarsBY, 0.);
		assertEquals(orderBasket.totalDollarsBTC, 50000.);
		assertEquals(orderBasket.totalDollarsSL, 15000.);
		assertEquals(orderBasket.totalDollarsSS, 0.);

		assertEquals(orderBasket.execDollarsBY, 0.);
		assertEquals(orderBasket.execDollarsBTC, 30000.);
		assertEquals(orderBasket.execDollarsSL, 15000.);
		assertEquals(orderBasket.execDollarsSS, 0.);

		assertEquals(orderBasket.getTotalShares(), 800);
		assertEquals(orderBasket.getOpenShares(), 200);
		assertEquals(orderBasket.getExecShares(), 600);
		assertEquals(orderBasket.nSharesSS, 0);
	}

	public void testTotalAbsNetDollars() {
		orderBasket.getSummary();
		assertEquals(orderBasket.getTotalDollarsAbs(), 65000.);
		assertEquals(orderBasket.getExecDollarsAbs(), 45000.);
		assertEquals(orderBasket.getExecDollarsNet(), 15000.);
		assertEquals(orderBasket.getTotalDollarsNet(), 35000.);
	}

	public void testOpenDollars() {
		orderBasket.getSummary();
		assertEquals(orderBasket.getOpenDollarsBY(), 0.);
		assertEquals(orderBasket.getOpenDollarsBTC(), 20000.);
		assertEquals(orderBasket.getOpenDollarsSL(), 0.);
		assertEquals(orderBasket.getOpenDollarsSS(), 0.);
	}
}











