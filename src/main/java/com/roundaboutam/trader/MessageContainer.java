package com.roundaboutam.trader;

import java.util.HashMap;

import quickfix.FieldNotFound;
import quickfix.Message;



public class MessageContainer {

	private final Message message;
	private final String MsgType;
	private final String Direction;
	private final Message.Header Header;
	private final String ClOrdID;
	private final String OrigClOrdID;
	private final String OrdType;
	private final String OrderID;
	private final String Symbol;
	private final String Side;
	private final String OrderQty;
	private final String OrdStatus;
	private final String ExecType;
	private final String LastShares;
	public HashMap<String, String> rawValues;


	public MessageContainer(Message message) {
		this.message = message;
		Header = message.getHeader();
		rawValues = new HashMap<String, String>();
		MsgType = resolveMsgType(message);
		Direction = resolveDirection();
		ClOrdID = resolveClOrdID(message);
		OrigClOrdID = resolveOrigClOrdID(message);
		OrdType = resolveOrdType(message);
		OrderID = resolveOrderID(message);
		Symbol = resolveSymbol(message);
		Side = resolveSide(message);
		OrdStatus = resolveOrdStatus(message);
		ExecType = resolveExecType(message);
		OrderQty = resolveOrderQty(message);
		LastShares = resolveLastShares(message);
    }


	public String getPermID() {
		// THIS DOES NOT WORK IN ALL CASES, I.E DOUBLE REPLACE
    	String msgTypeVal = (String) rawValues.get("MsgType");
    	if(quickfix.field.MsgType.ORDER_SINGLE.equals(msgTypeVal)) {
    		return ClOrdID;
    	}
    	else if("#NA".equals(OrigClOrdID)) {
    		return ClOrdID;
    	}
    	return OrigClOrdID;
    }

	public String getMsgQty() {
    	String execTypeVal = (String) rawValues.get("ExecType");
    	if(String.valueOf(quickfix.field.ExecType.PARTIAL_FILL).equals(execTypeVal)) {
    		return LastShares;
    	}
    	return OrderQty;
    }
	
    public String getStatus() {
    	String msgTypeVal = (String) rawValues.get("MsgType");
    	switch(msgTypeVal) {
    	case quickfix.field.MsgType.EXECUTION_REPORT:
    		return ExecType;
    	case quickfix.field.MsgType.ORDER_CANCEL_REJECT:
    		return OrdStatus;
    	case quickfix.field.MsgType.ORDER_SINGLE:
    		return Side;
    	case quickfix.field.MsgType.ORDER_CANCEL_REPLACE_REQUEST:
    		return Side;
    	case quickfix.field.MsgType.ORDER_CANCEL_REQUEST:
    		return Side;	
    	}
    	return "#NA";
    }

	private String resolveMsgType(Message message) {
        try {
        	String msgTypeVal = message.getHeader().getString(quickfix.field.MsgType.FIELD);
        	rawValues.put("MsgType", msgTypeVal);
        	switch (msgTypeVal) {
        	case quickfix.field.MsgType.EXECUTION_REPORT:
        		return "ExecRept";
        	case quickfix.field.MsgType.ORDER_CANCEL_REJECT:
        		return "CancelOrReject";
        	case quickfix.field.MsgType.ORDER_SINGLE:
        		return "NewOrder";
        	case quickfix.field.MsgType.ORDER_CANCEL_REPLACE_REQUEST:
        		return "ReplaceOrder";
        	case quickfix.field.MsgType.ORDER_CANCEL_REQUEST:
        		return "CancelOrder";
        	}
        	return msgTypeVal;
		} catch (FieldNotFound e) {
			rawValues.put("MsgTpye", "#NA");
			return "#NA";
		}
	}

	private String resolveExecType(Message message) {
        try {
        	char execTypeVal = message.getChar(quickfix.field.ExecType.FIELD);
        	rawValues.put("ExecType", String.valueOf(execTypeVal));
        	switch (execTypeVal) {
        	case quickfix.field.ExecType.NEW:
        		return "OrderAck";
        	case quickfix.field.ExecType.PARTIAL_FILL:
        		return "PartialFill";	
        	case quickfix.field.ExecType.FILL:
        		return "Filled";	
        	case quickfix.field.ExecType.DONE_FOR_DAY:
        		return "DoneForDay";
        	case quickfix.field.ExecType.CANCELED:
        		return "CancelAck";
        	case quickfix.field.ExecType.REPLACE:
        		return "ReplaceAck";
        	case quickfix.field.ExecType.STOPPED:
        		return "Stopped";
        	case quickfix.field.ExecType.SUSPENDED:
        		return "Suspended";
        	case quickfix.field.ExecType.REJECTED:
        		return "Rejected";
        	case quickfix.field.ExecType.PENDING_CANCEL:
        		return "PendingCancel";
        	case quickfix.field.ExecType.PENDING_REPLACE:
        		return "PendingReplace";
        	case quickfix.field.ExecType.PENDING_NEW:
        		return "PendingNew";
        	case quickfix.field.ExecType.TRADE_IN_A_CLEARING_HOLD:
        		return "ClearingHold";
        	}
        	return String.valueOf(execTypeVal);
		} catch (FieldNotFound e) {
			rawValues.put("ExecType", "#NA");
			return "#NA";
		}
	}

	private String resolveOrdStatus(Message message) {
        try {
        	char ordStatusVal = message.getChar(quickfix.field.OrdStatus.FIELD);
        	rawValues.put("OrdStatus", String.valueOf(ordStatusVal));
        	switch (ordStatusVal) {
        	case quickfix.field.OrdStatus.NEW:
        		return "OrderAck";
        	case quickfix.field.OrdStatus.PARTIALLY_FILLED:
        		return "PartialFill";	
        	case quickfix.field.OrdStatus.FILLED:
        		return "Filled";	
        	case quickfix.field.OrdStatus.DONE_FOR_DAY:
        		return "DoneForDay";
        	case quickfix.field.OrdStatus.CANCELED:
        		return "Canceled";
        	case quickfix.field.OrdStatus.REPLACED:
        		return "Replaced";
        	case quickfix.field.OrdStatus.REJECTED:
        		return "Rejected";
        	case quickfix.field.OrdStatus.PENDING_CANCEL:
        		return "PendingCancel";
        	case quickfix.field.OrdStatus.PENDING_REPLACE:
        		return "PendingReplace";
        	case quickfix.field.OrdStatus.PENDING_NEW:
        		return "PendingNew";
        	}
        	return String.valueOf(ordStatusVal);
		} catch (FieldNotFound e) {
			rawValues.put("OrdStatus", "#NA");
			return "#NA";
		}
	}

	private String resolveSide(Message message) {
        try {
        	char sideVal = message.getChar(quickfix.field.Side.FIELD);
        	rawValues.put("Symbol", String.valueOf(sideVal));
        	switch (sideVal) {
        	case quickfix.field.Side.BUY:
        		return "Buy";
        	case quickfix.field.Side.SELL:
        		return "Sell";
        	case quickfix.field.Side.SELL_SHORT:
        		return "SellShort";
        	case quickfix.field.Side.SELL_SHORT_EXEMPT:
        		return "SellShortExempt";
        	}
        	return String.valueOf(sideVal);
		} catch (FieldNotFound e) {
        	rawValues.put("Symbol", "#NA");
			return "#NA";
		}
	}

	private String resolveSymbol(Message message) {
        try {
        	String symbolVal = message.getString(quickfix.field.Symbol.FIELD);
        	rawValues.put("Symbol", symbolVal);
        	return symbolVal;
		} catch (FieldNotFound e) {
        	rawValues.put("Symbol", "#NA");
			return "#NA";
		}
	}

	private String resolveOrderQty(Message message) {
        try {
        	String orderQtyVal = message.getString(quickfix.field.OrderQty.FIELD);
        	rawValues.put("OrderQty", orderQtyVal);
        	return orderQtyVal;
		} catch (FieldNotFound e) {
        	rawValues.put("Symbol", "#NA");
			return "#NA";
		}
	}

	private String resolveLastShares(Message message) {
        try {
        	String lastSharesVal = message.getString(quickfix.field.LastShares.FIELD);
        	rawValues.put("LastShares", lastSharesVal);
        	return lastSharesVal;
		} catch (FieldNotFound e) {
        	rawValues.put("Symbol", "#NA");
			return "#NA";
		}
	}

	private String resolveOrderID(Message message) {
        try {
        	String orderIDVal = message.getString(quickfix.field.OrderID.FIELD);
        	rawValues.put("OrderID", orderIDVal);
        	return orderIDVal;
		} catch (FieldNotFound e) {
			rawValues.put("OrderID", "#NA");
			return "#NA";
		}
	}

	private String resolveClOrdID(Message message) {
        try {
        	String clOrdIDVal = message.getString(quickfix.field.ClOrdID.FIELD);
        	rawValues.put("ClOrdID", clOrdIDVal);
        	return clOrdIDVal;
		} catch (FieldNotFound e) {
			rawValues.put("ClOrdID", "#NA");
			return "#NA";
		}
	}

	private String resolveOrigClOrdID(Message message) {
        try {
        	String origClOrdIDVal = message.getString(quickfix.field.OrigClOrdID.FIELD);
        	rawValues.put("OrigClOrdID", origClOrdIDVal);
        	return origClOrdIDVal;
		} catch (FieldNotFound e) {
			rawValues.put("OrigClOrdID", "#NA");
			return "#NA";
		}
	}

	private String resolveOrdType(Message message) {
        try {
        	String ordTypeVal = message.getString(quickfix.field.OrdType.FIELD);
        	rawValues.put("OrdType", ordTypeVal);
        	return ordTypeVal;
		} catch (FieldNotFound e) {
			rawValues.put("OrdType", "#NA");
			return "#NA";
		}
	}

	private String resolveDirection() {
		switch (MsgType) {
        	case "ExecRept":
        		return "Inbound";
        	case "CancelOrReject":
        		return "Inbound";
        	case "NewOrder":
        		return "Outbound";
        	case "ReplaceOrder":
        		return "Outbound";
        	case "CancelOrder":
        		return "Outbound";
        	}
			return "#NA";
	}

    public Message getMessage() {
        	return this.message;
        }
   
    public Message.Header getMessageHeader() {
    	return Header;
    }        

    public String getMsgType() {
    	return MsgType;
    }

    public String getDirection() {
    	return Direction;
    }

    public String getClOrdID() {
    	return ClOrdID;
    }

    public String getOrigClOrdID() {
    	return OrigClOrdID;
    }

    public String getOrderID() {
    	return OrderID;
    }

    public String getSymbol() {
    	return Symbol;
    }

    public String getSide() {
    	return Side;
    }

    public String getOrdStatus() {
    	return OrdStatus;
    }

    public String getOrderQty() {
    	return OrderQty;
    }

    public String getExecType() {
    	return ExecType;
    }

    public String getLastShares() {
    	return LastShares;
    }

    public String getOrdType() {
    	return OrdType;
    }

}

