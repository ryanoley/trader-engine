package com.roundaboutam.trader.execution;

import com.roundaboutam.trader.order.OrderSide;

public class Execution {

    private String symbol = null;
    private String suffix = null;

    private OrderSide side = null;
    
    private int quantity = 0;
    private double price;
    private double bid;
    private double ask;

    private String ID = null;
    private String orderID = null;
    private String exchangeID = null;
    private static int nextID = 1;
    
    public Execution(String orderID) {
    	ID = Integer.toString(nextID++);
    	this.orderID = orderID;
    }

    public String getID() {
    	return ID;
    }

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getExchangeID() {
		return exchangeID;
	}

	public void setExchangeID(String exchangeID) {
		this.exchangeID = exchangeID;
	}

}
