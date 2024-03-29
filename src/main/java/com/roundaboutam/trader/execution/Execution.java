package com.roundaboutam.trader.execution;

import java.util.StringJoiner;

import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.rmp.OrderSide;


public class Execution {
    private String ID;
    private String symbol;
    private String orderID;
    private String permanentID;
    private String transactTime;
    private double price;
    private int quantity;
    private String suffix = null;
    private String customTag = null;
    private OrderSide orderSide;
    private ExecutionType executionType;
 
    private static int nextID = 1;

    
    public Execution(String orderID, String permanentID, String symbol,
    		String transactTime, OrderSide orderSide,
    		int quantity, double price, ExecutionType executionType) {
    	ID = Integer.toString(nextID++);
    	this.orderID = orderID;
    	this.permanentID = permanentID;
    	this.symbol = symbol;
    	this.transactTime = transactTime;
    	this.quantity = quantity;
    	this.price = price;
    	this.orderSide = orderSide;
    	this.executionType = executionType;
    }

    
    public String getLogEntry() {
    	// Updated symbol if suffix
    	String outSymbol = symbol;
    	if (suffix != null) {
            outSymbol = outSymbol + "/" + suffix;
        }

    	StringJoiner joiner = new StringJoiner(",");
    	joiner.add(ID).add(executionType.toString()).add(orderID).add(permanentID).add(outSymbol).add(
    			transactTime).add(orderSide.toString()).add(Integer.toString(quantity)).add(
    			Double.toString(price)).add(customTag);
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

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
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
