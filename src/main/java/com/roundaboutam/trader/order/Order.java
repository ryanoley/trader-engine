package com.roundaboutam.trader.order;

import quickfix.SessionID;

public class Order {

	// Identification fields
    private String customTag = null;
	private String symbol = null;
    private String suffix = null;

    // Trading Session related fields
	private SessionID sessionID = null;
    private String orderID = null;
    private String permanentID = null;

    // Order related fields
    private OrderSide orderSide = null;
	private char openClose;
    private OrderType orderType = null;
    private OrderTIF orderTIF = null;

    // Order related fields
    private int quantity = 0;
    private Double limitPrice = null;
    private Double stopPrice = null;

    // Execution related fields - Same names as FIX fields
    private int leavesQty = 0;
    private int cumQty = 0;
    private double avgPx = 0.0;
    private int orderQty = 0;

    private boolean rejected = false;
    private boolean canceled = false;
    private boolean modified = false;
    private boolean acknowledged = false;

    private String FIXMessage = null;
    private String message = null;

    public Order() {
        orderID = IdGenerator.makeID();
        permanentID = orderID;
    }

    public Order(String newID) {
    	orderID = newID;
    	permanentID = newID;
    }

    public int processFill(int cumQty, int leavesQty, double avgPx, 
    		int orderQty, String FIXMessage) {
    	int fillSize = cumQty - getCumQty();
    	setCumQty(cumQty);
    	setAvgPx(avgPx);
    	setLeavesQty(leavesQty);
    	setOrderQty(orderQty);
    	setFIXMessage(FIXMessage);
    	checkExecution();
    	return fillSize;
    }

    private void checkExecution() {
    	if (quantity != orderQty) {
    		this.message = "Quantity Mismatch";
    		System.out.println("Quantity Mismatch: " + symbol);
    	}
    }

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		symbol = symbol.toUpperCase();
		String[] symbols = symbol.split("[\\p{Punct}\\s]+");
		this.symbol = symbols[0];
		if (symbols.length > 1)
			setSuffix(symbols[1]);
	}

	public String getSuffix() {
		return suffix;
	}

	private void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public SessionID getSessionID() {
		return sessionID;
	}

	public void setSessionID(SessionID sessionID) {
		this.sessionID = sessionID;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getPermanentID() {
		return permanentID;
	}

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide) {
		this.orderSide = orderSide;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public OrderTIF getOrderTIF() {
		return orderTIF;
	}

	public void setOrderTIF(OrderTIF orderTIF) {
		this.orderTIF = orderTIF;
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

	public int getLeavesQty() {
		return leavesQty;
	}

	public void setLeavesQty(int leavesQty) {
		this.leavesQty = leavesQty;
	}

	public int getCumQty() {
		return cumQty;
	}

	public void setCumQty(int cumQty) {
		this.cumQty = cumQty;
	}

	public double getAvgPx() {
		return avgPx;
	}

	public void setAvgPx(double avgPx) {
		this.avgPx = avgPx;
	}

	public double getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
		this.message = "Rejected";
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
		this.message = "Canceled";
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		this.message = "Modified";
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
		this.message = "Acked";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFIXMessage() {
		return FIXMessage;
	}

	public void setFIXMessage(String FIXMessage) {
		this.FIXMessage = FIXMessage;
	}

	public void setOpenClose(char openClose) {
		this.openClose = openClose;
	}

	public char getOpenClose() {
		return openClose;
	}

}
