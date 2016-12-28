package com.roundaboutam.app.orders;

public class Order {

	public Order(int orderId, String instrumentId, OrderType orderType, OrderSide orderSide, 
			int quantity) {
		this.orderId = orderId;
		this.instrumentId = instrumentId.toUpperCase();
		this.orderType = orderType;
		this.orderSide = orderSide;
		this.quantity = quantity;
	}

	// Limit constructor
	public Order(int orderId, String instrumentId, OrderType orderType, OrderSide orderSide, 
			int quantity, double limitPrice) {
		this(orderId, instrumentId, orderType, orderSide, quantity);
		if (orderType != OrderType.LIMIT)
			throw new IllegalArgumentException("LimitPrice not necessary for given orderType");
		this.limitPrice = limitPrice;
	}

	private int orderId;

	private String instrumentId;

	private OrderType orderType;

	private OrderSide orderSide;

	private int quantity;

	// Default values
	private double limitPrice = -9999.0;

	private int executedQuantity = 0;

	private double avgExecutedPrice = -9999.0;
	
	public void updateExecutedQuantity(int units, double price) {
		avgExecutedPrice = (avgExecutedPrice * executedQuantity + price * units) / 
				(executedQuantity + units);
		executedQuantity = executedQuantity + units;
	}

	public int getOrderId() {
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
