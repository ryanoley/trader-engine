
package com.roundaboutam.trader.order;


import java.util.ArrayList;
import java.util.HashMap;

import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.rmp.OrderSide;


public class OrderBasket {

	private HashMap<String, Order> orderMap;
	private String basketName;
	private String basketID;
	public boolean staged = false;
	public boolean live = false;
	public boolean filled = false;
	public boolean deleted = false;
	private int totalShares = 0;
	private int openShares = 0;
	private int execShares = 0;
	public int nBY = 0;
	public int nSL = 0;
	public int nSS = 0;
	public int nBTC = 0;
	public int shrBY = 0;
	public int shrSL = 0;
	public int shrSS = 0;
	public int shrBTC = 0;
	public int shrBYExec = 0;
	public int shrSLExec = 0;
	public int shrSSExec = 0;
	public int shrBTCExec = 0;

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
		if (isStaged()) {
			order.setOrderBasketID(basketID);
			order.setOrderBasketName(basketName);
			orderMap.put(order.getOrderID(), order);
		}
	}

	public void removeOrder(Order order) {
		orderMap.remove(order.getOrderID());
		order.setOrderBasketID(null);
		order.setOrderBasketName(null);
	}

	public void getSummary() {
		resetAttr();
		int ackSum = 0;
		for (Order order : orderMap.values()) {
			OrderSide orderSide = order.getOrderSide();
			OrderOpenClose orderOpenClose = order.getOrderOpenClose();
			int orderQty = order.getQuantity();	
			int orderQtyExec = order.getCumQty();

			if (orderSide == OrderSide.BUY && orderOpenClose == OrderOpenClose.OPEN) {
				nBY ++;
				shrBY += orderQty;
				shrBYExec += orderQtyExec;
			} else if (orderSide == OrderSide.BUY && orderOpenClose == OrderOpenClose.CLOSE) {
				nBTC ++;
				shrBTC += orderQty;
				shrBTCExec += orderQtyExec;
			} else if (orderSide == OrderSide.SHORT_SELL) {
				nSS ++;
				shrSS += orderQty;
				shrSSExec += orderQtyExec;
			} else if (orderSide == OrderSide.SELL) {
				nSL ++;
				shrSL += orderQty;
				shrSLExec += orderQtyExec;
			}
			totalShares += orderQty;
			openShares += order.getLeavesQty();
			execShares += orderQtyExec;
			ackSum += order.isAcknowledged() ? 1 : 0;
		}
		if (openShares == 0  && live && ackSum == orderMap.size()) {
			setFilled(true);
			setLive(false);
		}
	}

	public void resetAttr() {
		totalShares = 0;
		openShares = 0;
		execShares = 0;
		nBY = 0;
		nSL = 0;
		nSS = 0;
		nBTC = 0;
		shrBY = 0;
		shrSL = 0;
		shrSS = 0;
		shrBTC = 0;
		shrBYExec = 0;
		shrSLExec = 0;
		shrSSExec = 0;
		shrBTCExec = 0;
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
	
	public int getOpenShares() {
		return openShares;
	}
	
	public int getExecShares() {
		return execShares;
	}
	
	public int getTotalShares() {
		return totalShares;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}

