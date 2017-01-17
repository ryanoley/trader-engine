package com.roundaboutam.trader.execution;

import java.util.StringJoiner;

import com.roundaboutam.trader.order.OrderSide;

public class Execution {

    private String ID = null;
    private String symbol = null;
    private String suffix = null;
    private String orderID = null;
    private String permanentID = null;
    private String customTag = null;

    private String exchangeID = null;
    private String transactTime;
    private OrderSide orderSide = null;
    private int quantity = 0;
    private double price;
    private double bid;
    private double ask;

    private static int nextID = 1;

    public Execution(String orderID, String permanentID, String symbol,
    		String transactTime, String exchangeID, OrderSide orderSide,
    		int quantity, double price) {
    	ID = Integer.toString(nextID++);
    	this.orderID = orderID;
    	this.permanentID = permanentID;
    	this.symbol = symbol;
    	this.transactTime = transactTime;
    	this.exchangeID = exchangeID;
    	this.orderSide = orderSide;
    	this.quantity = quantity;
    	this.price = price;
    }

    public String getLogEntry() {
    	// Updated symbol if suffix
    	String outSymbol = symbol;
    	if (suffix != null) {
            outSymbol = outSymbol + "/" + suffix;
        }
    	StringJoiner joiner = new StringJoiner(",");
    	joiner.add(ID).add(orderID).add(permanentID).add(outSymbol).add(transactTime).add(
    			exchangeID).add(orderSide.toString()).add(Integer.toString(quantity)).add(
    			Double.toString(price)).add(Double.toString(bid)).add(Double.toString(ask)).add(customTag);
    	return joiner.toString();
    }

	public String getSymbol() {
		return symbol;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getTransactTime() {
		return transactTime;
	}
	
	public OrderSide getSide() {
		return orderSide;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}

	public String getID() {
		return ID;
	}

	public String getOrderID() {
		return orderID;
	}

	public String getPermanentID() {
		return permanentID;
	}

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
	}

	public String getExchangeID() {
		return exchangeID;
	}

}