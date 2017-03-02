
package com.roundaboutam.trader.order;


import java.util.HashMap;

import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderSide;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.ramfix.OrderType;
import com.roundaboutam.trader.ui.BasketSummaryFrame;

import quickfix.SessionID;


public class OrderBasket {

	private HashMap<String, Order> orderMap;
	private String basketName;
	private String basketID;
	public boolean isStaged = false;
	public boolean isLive = false;
	public int nBY = 0;
	public int nSL = 0;
	public int nSS = 0;
	public int nBTC = 0;
	public int shrBY = 0;
	public int shrSL = 0;
	public int shrSS = 0;
	public int shrBTC = 0;

	public OrderBasket() {
		orderMap = new HashMap<String, Order>();
		basketID = IdGenerator.makeID();
		isStaged = true;
	}
	
	public OrderBasket(String name) {
		orderMap = new HashMap<String, Order>();
		basketID = IdGenerator.makeID();
		basketName = name;
		isStaged = true;
	}

	public void addOrder(Order order) {
		orderMap.put(order.getOrderID(), order);
	}

	public void removeOrder(Order order) {
		orderMap.remove(order.getOrderID());
	}

	public void summarizeBasket() {
		nBY = 0;
		nSL = 0;
		nSS = 0;
		nBTC = 0;
		shrBY = 0;
		shrSL = 0;
		shrSS = 0;
		shrBTC = 0;

		if (orderMap.size() == 0)
			return;
		
		for (Order order : orderMap.values()) {
			OrderSide orderSide = order.getOrderSide();
			OrderOpenClose orderOpenClose = order.getOrderOpenClose();
			int orderQty = order.getQuantity();	

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

	public HashMap<String, Order> getOrderMap() {
		return orderMap;
	}
	
	public void logSend() {
		isStaged = false;
		isLive = true;
	}
	
	public String getBasketId() {
		return basketID;
	}
	
	public String getBasketName() {
		return basketName;
	}
	
	public void setBasketName(String basketName) {
		this.basketName = basketName;
	}

	
}

