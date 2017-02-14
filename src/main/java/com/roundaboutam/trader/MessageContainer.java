package com.roundaboutam.trader;

import java.util.HashMap;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.Message.Header;



public class MessageContainer {

	private final Message message;
	private final Message.Header Header;
	public HashMap<String, String> rawValues;
	private final String MsgType;
	private final String SenderCompID;
	private final String TargetCompID;
	private final String Direction;
	private final String ClOrdID;
	private final String OrigClOrdID;
	private final String OrdType;
	private final String OrderID;
	private final String OpenClose;
	private final String Symbol;
	private final String SymbolSfx;
	private final String Side;
	private final String OrderQty;
	private final String OrdStatus;
	private final String ExecType;
	private final String LastShares;
	private final String Text;
	private final String CumQty;
	private final String LeavesQty;
	private final String AvgPx;
	private final String LastPx;
	private final String TransactTime;
	private final String MsgSeqNum;



	public MessageContainer(Message message) {
		this.message = message;
		Header = message.getHeader();
		rawValues = new HashMap<String, String>();
		MsgType = resolveMsgType(message);
		Direction = resolveDirection();
		Side = resolveSide(message);
		OrdStatus = resolveOrdStatus(message);
		ExecType = resolveExecType(message);

		SenderCompID = resolveHeaderField(message.getHeader(), quickfix.field.SenderCompID.FIELD, "SenderCompID");
		TargetCompID = resolveHeaderField(message.getHeader(), quickfix.field.TargetCompID.FIELD, "TargetCompID");
		MsgSeqNum = resolveHeaderField(message.getHeader(), quickfix.field.MsgSeqNum.FIELD, "MsgSeqNum");
	
		OrderID = resolveMessageField(message, quickfix.field.OrderID.FIELD, "OrderID");
		ClOrdID = resolveMessageField(message, quickfix.field.ClOrdID.FIELD, "ClOrdID");
		OrigClOrdID = resolveMessageField(message, quickfix.field.OrigClOrdID.FIELD, "OrigClOrdID");
		OrdType = resolveMessageField(message, quickfix.field.OrdType.FIELD, "OrdType");
		OpenClose = resolveMessageField(message, quickfix.field.OpenClose.FIELD, "OpenClose");
		Symbol = resolveMessageField(message, quickfix.field.Symbol.FIELD, "Symbol");
		SymbolSfx = resolveMessageField(message, quickfix.field.SymbolSfx.FIELD, "SymbolSfx");
		OrderQty = resolveMessageField(message, quickfix.field.OrderQty.FIELD, "OrderQty");
		LastShares = resolveMessageField(message, quickfix.field.LastShares.FIELD, "LastShares");
		CumQty = resolveMessageField(message, quickfix.field.CumQty.FIELD, "CumQty");
		LeavesQty = resolveMessageField(message, quickfix.field.LeavesQty.FIELD, "LeavesQty");
		AvgPx = resolveMessageField(message, quickfix.field.AvgPx.FIELD, "AvgPx");
		LastPx = resolveMessageField(message, quickfix.field.LastPx.FIELD, "LastPx");
		Text = resolveMessageField(message, quickfix.field.Text.FIELD, "Text");
		TransactTime = resolveMessageField(message, quickfix.field.TransactTime.FIELD, "TransactTime");
    }

	private String resolveHeaderField(Header header, int fieldInt, String dictKey) {
        try {
        	String fieldVal = header.getString(fieldInt);
        	rawValues.put(dictKey, fieldVal);
        	return fieldVal;
		} catch (FieldNotFound e) {
			rawValues.put(dictKey, "FieldNotFound");
			return "FieldNotFound";
		}
	}	

	private String resolveMessageField(Message message, int fieldInt, String dictKey) {
        try {
        	String fieldVal = message.getString(fieldInt);
        	rawValues.put(dictKey, fieldVal);
        	return fieldVal;
		} catch (FieldNotFound e) {
			rawValues.put(dictKey, "FieldNotFound");
			return "FieldNotFound";
		}
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
        	case quickfix.field.MsgType.LOGON:
        		return "Logon";
        	case quickfix.field.MsgType.HEARTBEAT:
        		return "Heartbeat";
        	case quickfix.field.MsgType.SEQUENCE_RESET:
        		return "SequenceReset";
        	case quickfix.field.MsgType.RESEND_REQUEST:
        		return "ResendRequest";
        	case quickfix.field.MsgType.TEST_REQUEST:
        		return "TestRequest";
        	}
        	return msgTypeVal;
		} catch (FieldNotFound e) {
			rawValues.put("MsgTpye", "FieldNotFound");
			return "FieldNotFound";
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
			rawValues.put("ExecType", "FieldNotFound");
			return "FieldNotFound";
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
			rawValues.put("OrdStatus", "FieldNotFound");
			return "FieldNotFound";
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
        	rawValues.put("Symbol", "FieldNotFound");
			return "FieldNotFound";
		}
	}

	private String resolveDirection() {
    	String msgTypeVal = (String) rawValues.get("MsgType");
    	switch(msgTypeVal) {
    	case quickfix.field.MsgType.LOGON:;
    	case quickfix.field.MsgType.HEARTBEAT:
    	case quickfix.field.MsgType.EXECUTION_REPORT:
    	case quickfix.field.MsgType.ORDER_CANCEL_REJECT:
    		return "Inbound";
    	case quickfix.field.MsgType.ORDER_SINGLE:
    	case quickfix.field.MsgType.ORDER_CANCEL_REPLACE_REQUEST:
    	case quickfix.field.MsgType.ORDER_CANCEL_REQUEST:
    		return "Outbound";
    	}
    	return "Inbound";
	}

	public String getDisplayID() {
    	String msgTypeVal = rawValues.get("MsgType");
    	switch(msgTypeVal) {
		case quickfix.field.MsgType.EXECUTION_REPORT:
		case quickfix.field.MsgType.ORDER_CANCEL_REJECT:
			return OrderID;
		case quickfix.field.MsgType.ORDER_SINGLE:
		case quickfix.field.MsgType.ORDER_CANCEL_REPLACE_REQUEST:
		case quickfix.field.MsgType.ORDER_CANCEL_REQUEST:
		case quickfix.field.MsgType.HEARTBEAT:
		case quickfix.field.MsgType.LOGON:
		case quickfix.field.MsgType.SEQUENCE_RESET:
		case quickfix.field.MsgType.RESEND_REQUEST:
			return "-";	
    	}
    	return OrderID;
    }

	public String getMsgQty() {
    	String execTypeVal = rawValues.get("ExecType");
    	char execTypeChar = execTypeVal.charAt(0);
    	switch(execTypeChar) {
    	case quickfix.field.ExecType.PARTIAL_FILL:
    		return LastShares;
    	case quickfix.field.ExecType.FILL:
    		return LastShares;
    	}
    	return OrderQty;
    }

    public String getStatus() {
    	String msgTypeVal = rawValues.get("MsgType");
    	switch(msgTypeVal) {
    	case quickfix.field.MsgType.EXECUTION_REPORT:
    		return ExecType;
    	case quickfix.field.MsgType.ORDER_CANCEL_REJECT:
    		return OrdStatus;
    	case quickfix.field.MsgType.ORDER_SINGLE:
    	case quickfix.field.MsgType.ORDER_CANCEL_REPLACE_REQUEST:
    	case quickfix.field.MsgType.ORDER_CANCEL_REQUEST:
    		return Side;
		case quickfix.field.MsgType.HEARTBEAT:
		case quickfix.field.MsgType.LOGON:
		case quickfix.field.MsgType.SEQUENCE_RESET:
		case quickfix.field.MsgType.RESEND_REQUEST:
			return "-";	
    	}
    	return "FieldNotFound";
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

    public String getSymbolSfx() {
    	return SymbolSfx;
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

    public String getText() {
    	if ("FieldNotFound".equals(Text))
    		return "-";
    	else
    		return Text;
    }

    public String getCumQty() {
    	return CumQty;
    }

    public String getLeavesQty() {
    	return LeavesQty;
    }

    public String getAvgPx() {
    	return AvgPx;
    }

    public String getSenderCompID() {
    	return SenderCompID;
    }

    public String getTargetCompID() {
    	return TargetCompID;
    }
    
    public String getMsgSeqNum() {
    	return MsgSeqNum;
    }

    public String getTransactTime() {
    	return TransactTime;
    }
    
    public String getLastPx() {
    	return LastPx;
    }

    public String getOpenClose() {
    	return OpenClose;
    }
}

