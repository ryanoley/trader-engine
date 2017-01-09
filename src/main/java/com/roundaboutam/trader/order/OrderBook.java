package com.roundaboutam.trader.order;

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

	public int processExecutionReport(String orderID, int orderQty, int cumQty, int leavesQty,
			double avgPx, String orderMessage) {

		Order order = null;

		if (replaceOrderMap.containsKey(orderID)) {

			ReplaceOrder replaceOrder = replaceOrderMap.remove(orderID);
			// TODO: This should be logged
			replaceOrder.setAcknowledged(true);

			order = orderMap.get(replaceOrder.getOrigOrderID());

			order.setQuantity(replaceOrder.getQuantity());
			order.setLimitPrice(replaceOrder.getLimitPrice());
			order.setStopPrice(replaceOrder.getStopPrice());
			order.setOpen(order.getQuantity() - order.getExecuted());

			orderMap.put(replaceOrder.getOrderID(), order);

		} else if (cancelOrderMap.containsKey(orderID)) {

			CancelOrder cancelOrder = cancelOrderMap.remove(orderID);
			// TODO: This should be logged
			cancelOrder.setAcknowledged(true);

			order = orderMap.get(cancelOrder.getOrigOrderID());

			order.setCanceled(true);
			order.setOpen(0);

			orderMap.put(cancelOrder.getOrderID(), order);

		} else {
			order = orderMap.get(orderID);
		}

		// For debugging
		if (order.getQuantity() != orderQty) { 
			orderMessage = "QUANTITY MISMATCH";
			System.out.println(orderMessage);
		}

		int fillSize = order.processFill(cumQty, avgPx, orderMessage);

		if (order.getOpen() != leavesQty) {
			orderMessage = "SHARES REMAINING MISMATCH";
			System.out.println(orderMessage);			
		}

		return fillSize;
	}

	public void orderRejected(String orderID) {
		// TODO: Log this?
		Order order = orderMap.get(orderID);
		order.setRejected(true);
		order.setOpen(0);
	}

	public void orderCanceled(String orderID) {
		// TODO: Log this?
		Order order = orderMap.get(orderID);
		order.setCanceled(true);
		order.setOpen(0);		
	}

}
