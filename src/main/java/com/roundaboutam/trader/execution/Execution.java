package com.roundaboutam.trader.execution;

import java.util.StringJoiner;


public class Execution {
	  
    public static final String FILL = "F";
    public static final String CANCEL = "C";
    public static final String REPLACE = "RP";
    public static final String REJECT = "RJ";
    private String ID = null;
    private String symbol = null;
    private String suffix = null;
    private String orderID = null;
    private String permanentID = null;
    private String customTag = null;
    private String transactTime;
    private String orderSide = null;
    private String execType = null;
    private int quantity = 0;
    private double price;
    private double bid;
    private double ask;
 
    private static int nextID = 1;

    
    public Execution(String orderID, String permanentID, String symbol,
    		String transactTime, String orderSide,
    		int quantity, double price, String execType) {
    	ID = Integer.toString(nextID++);
    	this.orderID = orderID;
    	this.permanentID = permanentID;
    	this.symbol = symbol;
    	this.transactTime = transactTime;
    	this.orderSide = orderSide;
    	this.quantity = quantity;
    	this.price = price;
    	this.execType = execType;
    }

    
    public String getLogEntry() {
    	// Updated symbol if suffix
    	String outSymbol = symbol;
    	if (suffix != null) {
            outSymbol = outSymbol + "/" + suffix;
        }
    	StringJoiner joiner = new StringJoiner(",");
    	joiner.add(ID).add(execType).add(orderID).add(permanentID).add(outSymbol).add(
    			transactTime).add(orderSide).add(Integer.toString(quantity)).add(
    			Double.toString(price)).add(Double.toString(bid)).add(
    			Double.toString(ask)).add(customTag);
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
	
	public String getSide() {
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


}
