package com.roundaboutam.app.orders;

public class Order {

	private String orderId;
	private String orderIdReplace = null;
	private String orderIdCancel = null;
	private String instrumentId;
	private OrderType orderType;
	private OrderSide orderSide;
	private int quantity;
	private double limitPrice = -9999.0;
	private int executedQuantity = 0;
	private double avgExecutedPrice = -9999.0;

	public Order(String instrumentId, OrderType orderType, OrderSide orderSide, 
			int quantity) {
		this.orderId = OrderIdMaker.getNextId();
		this.instrumentId = instrumentId.toUpperCase();
		this.orderType = orderType;
		this.orderSide = orderSide;
		this.quantity = quantity;
	}

	// Limit constructor
	public Order(String instrumentId, OrderType orderType, OrderSide orderSide, 
			int quantity, double limitPrice) {
		this(instrumentId, orderType, orderSide, quantity);
		if (orderType != OrderType.LIMIT)
			throw new IllegalArgumentException("LimitPrice not necessary for given orderType");
		this.limitPrice = limitPrice;
	}

	// Interface for received orders
	public void updateExecutedQuantity(int reportQuantity, double reportPrice) {
		avgExecutedPrice = (avgExecutedPrice * executedQuantity + reportPrice * reportQuantity) / 
				(executedQuantity + reportQuantity);
		executedQuantity = executedQuantity + reportQuantity;
	}

	// Interface for replacing and cancelling orders
	public void replaceQuantity(int newQuantity) {
		replaceQuantityPrice(newQuantity, limitPrice);
	}

	public void replacePrice(double newPrice) {
		replaceQuantityPrice(quantity, newPrice);
	}

	public void cancelOrder() {
		orderIdCancel = OrderIdMaker.getNextId();
	}

	public String getCancelOrderId() {
		return orderIdCancel;
	}

	public void replaceQuantityPrice(int newQuantity, double newPrice) {
		// New OrderID must be created
		orderIdReplace = OrderIdMaker.getNextId();
		quantity = newQuantity;
		limitPrice = newPrice;
	}

	public String getReplaceOrderId() {
		orderId = orderIdReplace;
		orderIdReplace = null;
		return orderId;
	}

	// Getters
	public String getOrderId() {
		return orderId;
	}

	public String getInstrumentId() {
		return instrumentId;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public double getLimitPrice() {
		return limitPrice;
	}
	
	public int getExecutedQuantity() {
		return executedQuantity;
	}

	public double getAvgExecutedPrice() {
		return avgExecutedPrice;
	}

}
