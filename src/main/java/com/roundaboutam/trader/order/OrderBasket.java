
package com.roundaboutam.trader.order;


import java.util.HashMap;

import com.roundaboutam.trader.ui.BasketFrame;

import fix.OrderOpenClose;
import fix.OrderSide;
import fix.OrderTIF;
import fix.OrderType;
import quickfix.SessionID;


public class OrderBasket {

	private HashMap<String, Order> basketOrderMap;
	private BasketFrame basketFrame;
	
	public OrderBasket() {
		basketOrderMap = new HashMap<String, Order>();
	}
	
	
	public void showBasket() {
		basketFrame = BasketFrame.getInstance(this);
	}
	
	
	public void addOrder(Order order) {
		basketOrderMap.put(order.getOrderID(), order);
	}
	
	public void removeOrder(Order order) {
		basketOrderMap.remove(order.getOrderID());
	}
	
	public void sendBasket(){
		
	}
	
	public HashMap<String, Order> getBasketOrderMap() {
		return basketOrderMap;
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
        order.setOrderOpenClose(OrderOpenClose.OPEN);
        order.setSessionID(sessionID);
        orderBasket.addOrder(order);

    	Order order2 = new Order();
    	orderSide = OrderSide.SHORT_SELL;
    	order2.setOrderType(OrderType.MARKET);
    	order2.setSymbol("AAPL");
    	order2.setOrderQty(300);
    	order2.setOrderSide(orderSide);
    	order2.setOrderTIF(OrderTIF.DAY);
        order2.setOrderOpenClose(OrderOpenClose.OPEN);
    	order2.setSessionID(sessionID);
        orderBasket.addOrder(order2);
        orderBasket.showBasket();
    }
	
	
	
}






