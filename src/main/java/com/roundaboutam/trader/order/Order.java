package com.roundaboutam.trader.order;


import java.util.Map;
import java.util.StringJoiner;
import java.util.LinkedHashMap;

import com.roundaboutam.trader.MessageContainer;
import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import quickfix.SessionID;

public class Order {

	// Identification fields
    private String customTag = null;
	private String symbol = null;
    private String suffix = null;
    private Boolean vwapFlag = false;

    // Trading Session related fields
	private SessionID sessionID = null;
    private String orderID = null;
    private String permanentID = null;

    // Order related fields
    private OrderSide orderSide = null;
	private OrderOpenClose orderOpenClose = null;
    private PriceType priceType = null;
    private OrderTIF orderTIF = null;
    private Integer quantity = 0;
    private Double limitPrice = null;
    private Double stopPrice = null;
	private String startTime = "09:32:00";
	private String endTime = "15:58:00";
	private int participationRate = 12;

    // Execution related fields - Same names as FIX fields
    private Integer leavesQty = 0;
    private Integer cumQty = 0;
    private Double avgPx = 0.0;
    private Integer orderQty = 0;

    private boolean rejected = false;
    private boolean canceled = false;
    private boolean modified = false;
    private boolean acknowledged = false;
    private boolean oldSession = false;

    private String message = null;
    private String orderBasketName = null;
    private String orderBasketID = null;

    public Order() {
        orderID = IdGenerator.makeID();
        permanentID = orderID;
        setMessage("Created");
    }

    public Order(String newID) {
    	orderID = newID;
    	permanentID = newID;
    }

    public int processFill(int cumQty, int leavesQty, double avgPx, int orderQty) {
    	int fillSize = cumQty - getCumQty();
    	setCumQty(cumQty);
    	setAvgPx(avgPx);
    	setLeavesQty(leavesQty);
    	setOrderQty(orderQty);
    	checkExecution();
    	return fillSize;
    }

	public void updateMessage(MessageContainer messageContainer) {
		ExecutionType executionType = messageContainer.getExecutionType();
		StringJoiner joiner = new StringJoiner(" ");
		if (isOldSession())
			joiner.add("Old Session -");
		joiner.add(executionType.toString());
		setMessage(joiner.toString());
	}

    private void checkExecution() {
    	if (!quantity.equals(orderQty)) {
    		setMessage("Quantity Mismatch");
    		System.out.println("Quantity Mismatch: " + symbol);
    	}
    }

	public Map<String, String> getExportHash() {
		
		Map<String, String> exportHash = new LinkedHashMap<String, String>();
		
		exportHash.put("OrderID", this.orderID);
		exportHash.put("Symbol", this.symbol);
		exportHash.put("Suffix", this.suffix);
		exportHash.put("Side", this.orderSide.toString());
		exportHash.put("Type", this.priceType.toString());
		exportHash.put("Quantity", this.quantity.toString());
		exportHash.put("ExecutedShares", this.cumQty.toString());
		exportHash.put("OpenShares", this.leavesQty.toString());
		exportHash.put("AveragePrice", this.avgPx.toString());
		exportHash.put("BasketID", this.orderBasketID);
		exportHash.put("BasketName", this.orderBasketName);
		
		return exportHash;
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

	public PriceType getPriceType() {
		return priceType;
	}

	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
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

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
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
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}
	
	public boolean isOldSession() {
		return oldSession;
	}

	public void setOldSession(boolean acknowledged) {
		this.oldSession = acknowledged;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public OrderOpenClose getOrderOpenClose() {
		return orderOpenClose;
	}

	public void setOrderOpenClose(OrderOpenClose orderOpenClose) {
		this.orderOpenClose = orderOpenClose;
	}

	public String getOrderBasketName() {
		return orderBasketName;
	}

	public void setOrderBasketName(String orderBasketName) {
		this.orderBasketName = orderBasketName;
	}
	
	public String getOrderBasketID() {
		return orderBasketID;
	}

	public void setOrderBasketID(String orderBasketID) {
		this.orderBasketID = orderBasketID;
	}
	
	// Vwap order realated fields
	public Boolean getVwapFlag() {
		return vwapFlag;
	}

	public void setVwapFlag(Boolean vwapFlag) {
		this.vwapFlag = vwapFlag;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getParticipationRate() {
		return participationRate;
	}

	public void setParticipationRate(int participationRate) {
		this.participationRate = participationRate;
	}

	
}
