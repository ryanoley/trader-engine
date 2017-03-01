
package com.roundaboutam.trader.order;


import java.util.HashMap;

import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderSide;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.ramfix.OrderType;
import com.roundaboutam.trader.ui.BasketFrame;

import quickfix.SessionID;


public class OrderBasket {

	private HashMap<String, Order> basketOrderMap;
	private BasketFrame basketFrame;
	public int nBY = 0;
	public int nSL = 0;
	public int nSS = 0;
	public int nBTC = 0;
	public Double shrBY = 0.0;
	public Double shrSL = 0.0;
	public Double shrSS = 0.0;
	public Double shrBTC = 0.0;

	public OrderBasket() {
		basketOrderMap = new HashMap<String, Order>();
	}

	public void showBasket() {
		summarizeBasket();
		basketFrame = BasketFrame.getInstance(this);
	}

	public void addOrder(Order order) {
		basketOrderMap.put(order.getOrderID(), order);
	}

	public void removeOrder(Order order) {
		basketOrderMap.remove(order.getOrderID());
	}

	private void summarizeBasket() {
		nBY = 0;
		nSL = 0;
		nSS = 0;
		nBTC = 0;
		shrBY = 0.0;
		shrSL = 0.0;
		shrSS = 0.0;
		shrBTC = 0.0;

		if (basketOrderMap.size() ==0 )
			return;
		
		for (Order order : basketOrderMap.values()) {
			OrderSide orderSide = order.getOrderSide();
			OrderOpenClose orderOpenClose = order.getOrderOpenClose();
			Double orderQty = order.getOrderQty();	

			if (orderSide == OrderSide.BUY && orderOpenClose == OrderOpenClose.OPEN) {
				nBY ++;
				shrBY += orderQty;
			} else if (orderSide == OrderSide.BUY && orderOpenClose == OrderOpenClose.CLOSE) {
				nBTC ++;
				shrBTC += orderQty;
			} else if (orderSide == OrderSide.SHORT_SELL) {
				nSS ++;
				shrSS += orderQty;
			} else if (orderSide == OrderSide.SELL) {
				nSL ++;
				shrSL += orderQty;
			}
		}
	}

	public HashMap<String, Order> getBasketOrderMap() {
		return basketOrderMap;
	}
	
	public void sendOrders() {
		// TODO Auto-generated method stub
	}
	
	public static void main(String[] args) throws Exception {
		
		OrderBasket orderBasket = new OrderBasket();
    	Order order = new Order();
    	OrderSide orderSide = OrderSide.BUY;
    	SessionID sessionID = new SessionID("FIX.4.2:ROUNDTEST02->REALTICK2:RYAN");
        order.setOrderType(OrderType.MARKET);
        order.setSymbol("IBM");
        order.setOrderQty(100);
        order.setOrderSide(orderSide);
        order.setOrderTIF(OrderTIF.DAY);
        order.setLimitPrice(50.50);
        order.setOrderOpenClose(OrderOpenClose.OPEN);
        order.setSessionID(sessionID);
        orderBasket.addOrder(order);

    	Order order2 = new Order();
    	orderSide = OrderSide.SHORT_SELL;
    	order2.setOrderType(OrderType.MARKET);
    	order2.setSymbol("AAPL");
    	order2.setOrderQty(300);
        order2.setLimitPrice(51.50);
    	order2.setOrderSide(orderSide);
    	order2.setOrderTIF(OrderTIF.DAY);
        order2.setOrderOpenClose(OrderOpenClose.OPEN);
    	order2.setSessionID(sessionID);
        orderBasket.addOrder(order2);

    	Order order3 = new Order();
    	orderSide = OrderSide.SELL;
    	order3.setOrderType(OrderType.MARKET);
    	order3.setSymbol("GOOGL");
    	order3.setOrderQty(400);
        order3.setLimitPrice(3.50);
    	order3.setOrderSide(orderSide);
    	order3.setOrderTIF(OrderTIF.DAY);
    	order3.setOrderOpenClose(OrderOpenClose.OPEN);
    	order3.setSessionID(sessionID);
        orderBasket.addOrder(order3);

    	Order order4 = new Order();
    	orderSide = OrderSide.BUY;
    	order4.setOrderType(OrderType.MARKET);
    	order4.setSymbol("FB");
    	order4.setOrderQty(500);
        order4.setLimitPrice(4.50);
    	order4.setOrderSide(orderSide);
    	order4.setOrderTIF(OrderTIF.DAY);
    	order4.setOrderOpenClose(OrderOpenClose.OPEN);
    	order4.setSessionID(sessionID);
        orderBasket.addOrder(order4);
        
        orderBasket.showBasket();
    }
	
}






