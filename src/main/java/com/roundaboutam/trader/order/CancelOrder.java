package com.roundaboutam.trader.order;

import com.roundaboutam.trader.rmp.OrderSide;

import quickfix.SessionID;

public class CancelOrder {

	// Identification fields
	private String symbol = null;

    // Trading Session related fields
	private SessionID sessionID = null;
    private String orderID = null;
    private String origOrderID = null;

    // Order related fields
    private OrderSide orderSide = null;
    private int quantity = 0;

    // Execution related fields
    private boolean rejected = false;
    private boolean canceled = false;
    private boolean acknowledged = false;

    public CancelOrder(Order order) {
    	this.sessionID = order.getSessionID();
    	this.orderID = IdGenerator.makeID();
    	this.origOrderID = order.getOrderID();
    	this.symbol = order.getSymbol();
    	this.orderSide = order.getOrderSide();
    	this.quantity = order.getQuantity();
    	order.setCanceled(true);
    	order.setMessage("CancelReq");
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
	
	public int getQuantity() {
		return quantity;
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

}
