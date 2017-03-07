
package com.roundaboutam.trader.order;


import java.util.ArrayList;
import java.util.HashMap;

import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderSide;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.ramfix.OrderType;
import com.roundaboutam.trader.ui.BasketInfoFrame;

import quickfix.SessionID;


public class OrderBasket {

	private HashMap<String, Order> orderMap;
	private String basketName;
	private String basketID;
	public boolean staged = false;
	public boolean live = false;
	public boolean filled = false;
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
		setStaged(true);
	}
	
	public OrderBasket(String name) {
		orderMap = new HashMap<String, Order>();
		basketID = IdGenerator.makeID();
		basketName = name;
		setStaged(true);
	}

	public void addOrder(Order order) {
		order.setOrderBasketID(basketID);
		order.setOrderBasketName(basketName);
		orderMap.put(order.getOrderID(), order);
	}

	public void removeOrder(Order order) {
		orderMap.remove(order.getOrderID());
		order.setOrderBasketID(null);
		order.setOrderBasketName(null);
	}

	public void getSummary() {
		nBY = 0;
		nSL = 0;
		nSS = 0;
		nBTC = 0;
		shrBY = 0;
		shrSL = 0;
		shrSS = 0;
		shrBTC = 0;

		int leavesShares = 0;
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
			leavesShares += order.getLeavesQty();
		}
		if (leavesShares == 0  && live)
			setFilled(true);
		else 
			setFilled(false);			
	}

	public HashMap<String, Order> getOrderMap() {
		return orderMap;
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
	
	public boolean isStaged() {
		return staged;
	}

	public void setStaged(boolean staged) {
		this.staged = staged;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	
	public ArrayList<Order> getAllOrders() {
		ArrayList<Order> allOrders = new ArrayList<Order>();
		for (Order o : orderMap.values()) {
			allOrders.add(o);
		}
		return allOrders;
	}

	public ArrayList<Order> getAllOpenOrders() {
		ArrayList<Order> openOrders = new ArrayList<Order>();
		for (Order o : orderMap.values()) {
			if (o.getLeavesQty() > 0)
				openOrders.add(o);
		}
		return openOrders;
	}
	
}

