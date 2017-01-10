package com.roundaboutam.trader.order;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBook {

	private final HashMap<String, Order> orderMap;
	private final HashMap<String, ReplaceOrder> replaceOrderMap;
	private final HashMap<String, CancelOrder> cancelOrderMap;

	public OrderBook() {
		orderMap = new HashMap<String, Order>();
		replaceOrderMap = new HashMap<String, ReplaceOrder>();
		cancelOrderMap = new HashMap<String, CancelOrder>();
	}

	public void addOrder(Order order) {
		orderMap.put(order.getOrderID(), order);
	}

	public void addReplaceOrder(ReplaceOrder replaceOrder) {
		replaceOrderMap.put(replaceOrder.getOrderID(), replaceOrder);
	}

	public void addCancelOrder(CancelOrder cancelOrder) {
		cancelOrderMap.put(cancelOrder.getOrderID(), cancelOrder);
	}

	public Order getOrder(String orderID) {
			return orderMap.get(orderID);
	}

	public void cancelRejected(String orderID) {
		// TODO: Log these rejects
		if (replaceOrderMap.containsKey(orderID)) {
			ReplaceOrder replaceOrder = replaceOrderMap.remove(orderID);
			replaceOrder.setRejected(true);
			orderMap.get(replaceOrder.getOrigOrderID()).setMessage("Replace Rejected");
		} else if (cancelOrderMap.containsKey(orderID)) {
			CancelOrder cancelOrder = cancelOrderMap.remove(orderID);
			cancelOrder.setRejected(true);
			orderMap.get(cancelOrder.getOrigOrderID()).setMessage("Cancel Rejected");
		}
	}

	public int processExecutionReport(String orderID, String symbol, int orderQty, int cumQty, 
			int leavesQty, double avgPx, String FIXMessage) {

		Order order = null;

		if (replaceOrderMap.containsKey(orderID)) {
			ReplaceOrder replaceOrder = replaceOrderMap.remove(orderID);
			// TODO: This should be logged
			replaceOrder.setAcknowledged(true);

			order = orderMap.get(replaceOrder.getOrigOrderID());

			order.setQuantity(replaceOrder.getQuantity());
			order.setLimitPrice(replaceOrder.getLimitPrice());
			order.setStopPrice(replaceOrder.getStopPrice());

			orderMap.put(replaceOrder.getOrderID(), order);

		} else if (cancelOrderMap.containsKey(orderID)) {
			CancelOrder cancelOrder = cancelOrderMap.remove(orderID);
			// TODO: This should be logged
			cancelOrder.setAcknowledged(true);

			order = orderMap.get(cancelOrder.getOrigOrderID());
			order.setCanceled(true);
			orderMap.put(cancelOrder.getOrderID(), order);

		} else if (orderMap.containsKey(orderID)) {
			order = orderMap.get(orderID);

		} else {
			order = new Order(orderID);
			order.setSymbol(symbol);
			order.setOrderID(orderID);
			order.setMessage("Order from old session");
			addOrder(order);
		}

		return order.processFill(cumQty, leavesQty, avgPx, orderQty, FIXMessage);
	}

	public void orderRejected(String orderID) {
		// TODO: Log this?
		Order order = orderMap.get(orderID);
		order.setRejected(true);
		order.setLeavesQty(0);
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
