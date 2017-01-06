package com.roundaboutam.trader.order;

import java.util.HashMap;

public class OrderBook {

	private final HashMap<String, Order> orderMap;
	private final HashMap<String, ReplaceOrder> replaceOrderMap;
	private final HashMap<String, CancelOrder> cancelOrderMap;
	private final HashMap<String, Boolean> cancelReplaceMap;

	public OrderBook() {
		orderMap = new HashMap<String, Order>();
		replaceOrderMap = new HashMap<String, ReplaceOrder>();
		cancelOrderMap = new HashMap<String, CancelOrder>();
		cancelReplaceMap = new HashMap<String, Boolean>();
	}

	public void addOrder(Order order) {
		orderMap.put(order.getOrderID(), order);
		cancelReplaceMap.put(order.getOrderID(), false);
	}

	public void addReplaceOrder(ReplaceOrder replaceOrder) {
		replaceOrderMap.put(replaceOrder.getOrderID(), replaceOrder);
		cancelReplaceMap.put(replaceOrder.getOrderID(), true);
	}

	public void addCancelOrder(CancelOrder cancelOrder) {
		cancelOrderMap.put(cancelOrder.getOrderID(), cancelOrder);
		cancelReplaceMap.put(cancelOrder.getOrderID(), true);
	}

	public boolean checkCancelReplace(String orderID) {
		return cancelReplaceMap.get(orderID);
	}

	public Order getOrder(String orderID) {
		if (replaceOrderMap.containsKey(orderID))
			return orderMap.get(replaceOrderMap.get(orderID).getOrigOrderID());
		else if (replaceOrderMap.containsKey(orderID))
			return orderMap.get(cancelOrderMap.get(orderID).getOrigOrderID());
		else
			return orderMap.get(orderID);
	}

	public void cancelRejected(String orderID) {
		if (replaceOrderMap.containsKey(orderID)) {
			ReplaceOrder replaceOrder = replaceOrderMap.remove(orderID);
			orderMap.get(replaceOrder.getOrigOrderID()).setModified(false);
			orderMap.get(replaceOrder.getOrigOrderID()).setMessage("Replace Rejected");
		} else if (cancelOrderMap.containsKey(orderID)) {
			CancelOrder cancelOrder = cancelOrderMap.remove(orderID);
			orderMap.get(cancelOrder.getOrigOrderID()).setCanceled(false);
			orderMap.get(cancelOrder.getOrigOrderID()).setMessage("Cancel Rejected");
		}
	}

}
