
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
	
	public int nOrdersBY = 0;
	public int nOrdersBTC = 0;
	public int nOrdersSL = 0;
	public int nOrdersSS = 0;

	public int nSharesBY = 0;
	public int nSharesBTC = 0;
	public int nSharesSL = 0;
	public int nSharesSS = 0;

	public int execSharesBY = 0;
	public int execSharesBTC = 0;
	public int execSharesSL = 0;
	public int execSharesSS = 0;

	public double totalDollarsBY = 0;
	public double totalDollarsBTC = 0;
	public double totalDollarsSL = 0;
	public double totalDollarsSS = 0;

	public double execDollarsBY = 0;
	public double execDollarsBTC = 0;
	public double execDollarsSL = 0;
	public double execDollarsSS = 0;

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
			double orderAvgPx = order.getAvgPx();

			if (orderSide == OrderSide.BUY && orderOpenClose == OrderOpenClose.OPEN) {
				nOrdersBY ++;
				nSharesBY += orderQty;
				execSharesBY += orderQtyExec;
				totalDollarsBY += orderQty * orderAvgPx;
				execDollarsBY += orderQtyExec * orderAvgPx;
			} else if (orderSide == OrderSide.BUY && orderOpenClose == OrderOpenClose.CLOSE) {
				nOrdersBTC ++;
				nSharesBTC += orderQty;
				execSharesBTC += orderQtyExec;
				totalDollarsBTC += orderQty * orderAvgPx;
				execDollarsBTC += orderQtyExec * orderAvgPx;
			} else if (orderSide == OrderSide.SHORT_SELL) {
				nOrdersSS ++;
				nSharesSS += orderQty;
				execSharesSS += orderQtyExec;
				totalDollarsSS += orderQty * orderAvgPx;
				execDollarsSS += orderQtyExec * orderAvgPx;
			} else if (orderSide == OrderSide.SELL) {
				nOrdersSL ++;
				nSharesSL += orderQty;
				execSharesSL += orderQtyExec;
				totalDollarsSL += orderQty * orderAvgPx;
				execDollarsSL += orderQtyExec * orderAvgPx;
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
		
		nOrdersBY = 0;
		nOrdersBTC = 0;
		nOrdersSL = 0;
		nOrdersSS = 0;
		
		nSharesBY = 0;
		nSharesBTC = 0;
		nSharesSL = 0;
		nSharesSS = 0;
		
		execSharesBY = 0;
		execSharesBTC = 0;
		execSharesSL = 0;
		execSharesSS = 0;
		
		totalDollarsBY = 0;
		totalDollarsBTC = 0;
		totalDollarsSL = 0;
		totalDollarsSS = 0;
		
		execDollarsBY = 0;
		execDollarsBTC = 0;
		execDollarsSL = 0;
		execDollarsSS = 0;
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
	
	public int getOrderCount() {
		return orderMap.size();
	}

	public int getTotalShares() {
		return totalShares;
	}

	public int getExecShares() {
		return execShares;
	}

	public int getOpenShares() {
		return openShares;
	}	

	public int getOpenSharesBY() {
		return nSharesBY - execSharesBY;
	}
	
	public int getOpenSharesBTC() {
		return nSharesBTC - execSharesBTC;
	}
	
	public int getOpenSharesSL() {
		return nSharesSL - execSharesSL;
	}
	
	public int getOpenSharesSS() {
		return nSharesSS - execSharesSS;
	}
	
	public double getTotalDollarsAbs() {
		 return totalDollarsBY + totalDollarsBTC + totalDollarsSS + totalDollarsSL;
	}

	public double getTotalDollarsNet() {
		 return totalDollarsBY + totalDollarsBTC - totalDollarsSS - totalDollarsSL;
	}

	public double getExecDollarsAbs() {
		 return execDollarsBY + execDollarsBTC + execDollarsSS + execDollarsSL;
	}

	public double getExecDollarsNet() {
		 return execDollarsBY + execDollarsBTC - execDollarsSS - execDollarsSL;
	}
		
	public double getOpenDollarsBY() {
		return totalDollarsBY - execDollarsBY;
	}
	
	public double getOpenDollarsBTC() {
		return totalDollarsBTC - execDollarsBTC;
	}
	
	public double getOpenDollarsSL() {
		return totalDollarsSL - execDollarsSL;
	}
	
	public double getOpenDollarsSS() {
		return totalDollarsSS - execDollarsSS;
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

