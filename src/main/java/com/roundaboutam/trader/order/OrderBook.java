package com.roundaboutam.trader.order;

import java.util.HashMap;

public class OrderBook {

	private final HashMap<String, BaseOrder> idToOrder;
	private final HashMap<String, String> replaceIdMap;
	private final HashMap<String, String> cancelIdMap;

	public OrderBook() {
		idToOrder = new HashMap<String, BaseOrder>();
		replaceIdMap = new HashMap<String, String>();
		cancelIdMap = new HashMap<String, String>();
	}

	public void addOrder(Order order) {
		idToOrder.put(order.getOrderID(), order);
	}
	
	public void addReplaceOrder(ReplaceOrder replaceOrder) {
		idToOrder.put(replaceOrder.getOrderID(), replaceOrder);
		replaceIdMap.put(replaceOrder.getOrderID(), replaceOrder.getOrigOrderID());
	}

	public void addCancelOrder(CancelOrder cancelOrder) {
		idToOrder.put(cancelOrder.getOrderID(), cancelOrder);
		cancelIdMap.put(cancelOrder.getOrderID(), cancelOrder.getOrigOrderID());
	}

	public BaseOrder getOrder(String orderId) {
		return idToOrder.get(orderId);
	}

	public void cancelRejected(String cancelId) {
		String orderId = cancelIdMap.remove(cancelId);
		idToOrder.remove(cancelId);
		((Order) idToOrder.get(orderId)).setCanceled(false);
		((Order) idToOrder.get(orderId)).setMessage("Cancel Rejected");
	}

	public void DEBUG() {
		System.out.println(idToOrder);
		System.out.println(replaceIdMap);
		System.out.println(cancelIdMap);
	}
}
