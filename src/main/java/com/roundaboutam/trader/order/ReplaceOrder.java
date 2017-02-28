package com.roundaboutam.trader.order;

import quickfix.SessionID;
import ramfix.OrderSide;
import ramfix.OrderType;

public class ReplaceOrder {

	// Identification fields
	private String symbol = null;

    // Trading Session related fields
	private SessionID sessionID = null;
    private String orderID = null;
    private String origOrderID = null;

    // Order related fields
    private OrderSide orderSide = null;
    private OrderType orderType = null;

    // Order related fields
    private int quantity = 0;
    private Double limitPrice = null;
    private Double stopPrice = null;

    // Execution related fields
    private boolean rejected = false;
    private boolean canceled = false;
    private boolean acknowledged = false;

    public ReplaceOrder(Order order) {
    	this.symbol = order.getSymbol();
    	this.sessionID = order.getSessionID();
    	this.orderID = IdGenerator.makeID();
    	this.origOrderID = order.getOrderID();
    	this.orderSide = order.getOrderSide();
    	this.orderType = order.getOrderType();
    	this.quantity = order.getQuantity();
    	this.limitPrice = order.getLimitPrice();
    	this.stopPrice = order.getStopPrice();
    	order.setModified(true);
    	order.setMessage("ReplaceReq");
    }

    public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}

	public Double getStopPrice() {
		return stopPrice;
	}

	public void setStopPrice(Double stopPrice) {
		this.stopPrice = stopPrice;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public String getSymbol() {
		return symbol;
	}

	public SessionID getSessionID() {
		return sessionID;
	}

	public String getOrderID() {
		return orderID;
	}

	public String getOrigOrderID() {
		return origOrderID;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public OrderType getOrderType() {
		return orderType;
	}

}
