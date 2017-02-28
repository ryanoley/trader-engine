package com.roundaboutam.trader.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import com.roundaboutam.trader.MessageContainer;
import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.ramfix.OrderSide;
import com.roundaboutam.trader.ramfix.OrderType;

import quickfix.SessionID;
import quickfix.field.OrdType;
import quickfix.field.Side;

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

	public void processOrderCancelReject(String orderID) {
		if (replaceOrderMap.containsKey(orderID)) {
			ReplaceOrder replaceOrder = replaceOrderMap.remove(orderID);
			replaceOrder.setRejected(true);
			orderMap.get(replaceOrder.getOrigOrderID()).setMessage("Replace Rejected");
			orderMap.get(replaceOrder.getOrigOrderID()).setModified(false);
		} else if (cancelOrderMap.containsKey(orderID)) {
			CancelOrder cancelOrder = cancelOrderMap.remove(orderID);
			cancelOrder.setRejected(true);
			orderMap.get(cancelOrder.getOrigOrderID()).setMessage("Cancel Rejected");
			orderMap.get(cancelOrder.getOrigOrderID()).setCanceled(false);
		}
	}

	public int processExecutionReport(MessageContainer messageContainer, SessionID sessionID) {
		Order order;
		String orderID = messageContainer.getClOrdID();
		int cumQty = messageContainer.getCumQty();
		int leavesQty = messageContainer.getLeavesQty();
		int orderQty = messageContainer.getOrderQty();
		double avgPx = messageContainer.getAvgPx();

		if (replaceOrderMap.containsKey(orderID)) {
			ReplaceOrder replaceOrder = replaceOrderMap.remove(orderID);
			replaceOrder.setAcknowledged(true);
			order = orderMap.get(replaceOrder.getOrigOrderID());
			order.setQuantity(replaceOrder.getQuantity());
			order.setLimitPrice(replaceOrder.getLimitPrice());
			order.setStopPrice(replaceOrder.getStopPrice());
			order.setModified(true);
			orderMap.put(replaceOrder.getOrderID(), order);

		} else if (cancelOrderMap.containsKey(orderID)) {
			CancelOrder cancelOrder = cancelOrderMap.remove(orderID);
			cancelOrder.setAcknowledged(true);
			order = orderMap.get(cancelOrder.getOrigOrderID());
			order.setCanceled(true);
			orderMap.put(cancelOrder.getOrderID(), order);

		} else if (orderMap.containsKey(orderID)) {
			order = orderMap.get(orderID);
			order.setAcknowledged(true);

		} else {
			order = new Order(orderID);
			OrderSide orderSide = messageContainer.getOrderSide();
			OrderType orderType = messageContainer.getOrderType();
			order.setSymbol(messageContainer.getSymbol());
			order.setOrderID(orderID);
			order.setOrderSide(orderSide);
			order.setQuantity(orderQty);
			order.setOrderType(orderType);
			order.setSessionID(sessionID);
			order.setOldSession(true);
			order.setAcknowledged(true);
			addOrder(order);
		}
		updateOrderMessage(order, messageContainer);
		return order.processFill(cumQty, leavesQty, avgPx, orderQty);
	}

	public void updateOrderMessage(Order order, MessageContainer messageContainer) {
		ExecutionType executionType = messageContainer.getExecutionType();
		StringJoiner joiner = new StringJoiner(" ");
		if (order.isOldSession())
			joiner.add("Old Session -");

		joiner.add(executionType.toString());
		order.setMessage(joiner.toString());
	}

	public void orderRejected(String orderID) {
		Order order = orderMap.get(orderID);
		order.setRejected(true);
		order.setLeavesQty(0);
		order.setMessage("Rejected");
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

