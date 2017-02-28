package com.roundaboutam.trader.execution;

import java.util.StringJoiner;

import com.roundaboutam.trader.ramfix.OrderSide;

public class OrderCancelReject {

	private String ID;
    private String orderID;
    private String permanentID;
    private String symbol;
    private OrderSide orderSide;
    private String text;
    private String transactTime;
    private String suffix = null;
    private String customTag = null;
    private static int nextID = 1;


    public OrderCancelReject(String orderID, String permanentID, String symbol,
    		String transactTime, OrderSide orderSide, String text) {
    	ID = Integer.toString(nextID++);
    	this.orderID = orderID;
    	this.permanentID = permanentID;
    	this.symbol = symbol;
    	this.transactTime = transactTime;
    	this.orderSide = orderSide;
    	this.text = text;

    }
    
    public String getLogEntry() {
    	// Updated symbol if suffix
    	String outSymbol = symbol;
    	if (suffix != null) {
            outSymbol = outSymbol + "/" + suffix;
        }
    	StringJoiner joiner = new StringJoiner(",");
    	joiner.add(ID).add(orderID).add(permanentID).add(outSymbol).add(
    			transactTime).add(orderSide.toString()).add(customTag).add(text);
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
	
	public OrderSide getOrderSide() {
		return orderSide;
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

	
}
