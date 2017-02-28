package com.roundaboutam.trader;

import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.ramfix.FIXMessage;
import com.roundaboutam.trader.ramfix.MessageType;
import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderSide;
import com.roundaboutam.trader.ramfix.OrderStatus;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.ramfix.OrderType;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.Message.Header;
import quickfix.field.ExecType;
import quickfix.field.MsgType;
import quickfix.field.OpenClose;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;



public class MessageContainer {

	private final Message message;
	private final Message.Header header;
	private MessageType messageType;
	private OrderStatus orderStatus;
	private ExecutionType executionType;
	private OrderSide orderSide;
	private OrderType orderType;
	private OrderOpenClose orderOpenClose;
	private OrderTIF orderTIF;

	private int msgSeqNum;
	private String senderCompID;
	private String targetCompID;
	private String direction;
	private String clOrdID;
	private String origClOrdID;
	private String orderID;
	private String symbol;
	private String symbolSfx;
	private String sendingTime;
	private String transactTime;
	private String text;
	
	private Integer orderQty;
	private Integer lastShares;
	private Integer cumQty;
	private Integer leavesQty;
	private Double avgPx;
	private Double lastPx;



	public MessageContainer(Message message) {
		this.message = message;
		header = message.getHeader();
		resolveMsgType(message);
		resolveExecType(message);
		resolveOrdStatus(message);
		resolveSide(message);
		resolveOrderType(message);
		resolveOpenClose(message);
		resolveOrderTIF(message);
	
		senderCompID = resolveHeaderField(header, quickfix.field.SenderCompID.FIELD);
		targetCompID = resolveHeaderField(header, quickfix.field.TargetCompID.FIELD);
		sendingTime = resolveHeaderField(header, quickfix.field.SendingTime.FIELD);
		msgSeqNum = Integer.parseInt(resolveHeaderField(header, quickfix.field.MsgSeqNum.FIELD));

		orderID = resolveMessageField(message, quickfix.field.OrderID.FIELD);
		clOrdID = resolveMessageField(message, quickfix.field.ClOrdID.FIELD);
		origClOrdID = resolveMessageField(message, quickfix.field.OrigClOrdID.FIELD);
		symbol = resolveMessageField(message, quickfix.field.Symbol.FIELD);
		symbolSfx = resolveMessageField(message, quickfix.field.SymbolSfx.FIELD);
		text = resolveMessageField(message, quickfix.field.Text.FIELD);
		transactTime = resolveMessageField(message, quickfix.field.TransactTime.FIELD);

		orderQty = toInt(resolveMessageField(message, quickfix.field.OrderQty.FIELD));
		lastShares = toInt(resolveMessageField(message, quickfix.field.LastShares.FIELD));
		cumQty = toInt(resolveMessageField(message, quickfix.field.CumQty.FIELD));
		leavesQty = toInt(resolveMessageField(message, quickfix.field.LeavesQty.FIELD));
		avgPx = toDouble(resolveMessageField(message, quickfix.field.AvgPx.FIELD));
		lastPx = toDouble(resolveMessageField(message, quickfix.field.LastPx.FIELD));

		resolveDirection();
    }
		
	private void resolveMsgType(Message message) {
        try {
        	MsgType fixMsgType = (MsgType) message.getHeader().getField(new MsgType());
        	this.messageType = FIXMessage.FIXMsgTypeToMessageType(fixMsgType);
		} catch (FieldNotFound e) {
			this.messageType = null;
		}
	}

	private void resolveExecType(Message message) {
        try {
        	ExecType fixExecType = (ExecType) message.getField(new ExecType());
        	this.executionType = FIXMessage.FIXExecTypeToExecutionType(fixExecType);
		} catch (FieldNotFound e) {
			this.executionType = null;
		}
	}

	private void resolveOrdStatus(Message message) {
        try {
        	OrdStatus fixOrdStatus = (OrdStatus) message.getField(new OrdStatus());
        	this.orderStatus = FIXMessage.FIXOrdStatusToOrderStatus(fixOrdStatus);
		} catch (FieldNotFound e) {
			this.orderStatus = null;
		}
	}

	private void resolveSide(Message message) {
        try {
        	Side fixSide = (Side) message.getField(new Side());
        	this.orderSide = FIXMessage.FIXSideToOrderSide(fixSide);
		} catch (FieldNotFound e) {
			this.orderSide = null;
		}
	}

	private void resolveOrderType(Message message) {
        try {
        	OrdType fixOrdType = (OrdType) message.getField(new OrdType());
        	this.orderType = FIXMessage.FIXOrdTypeToOrderType(fixOrdType);
		} catch (FieldNotFound e) {
			this.orderType = null;
		}
	}

	private void resolveOpenClose(Message message) {
        try {
        	OpenClose fixOpenClose = (OpenClose) message.getField(new OpenClose());
        	this.orderOpenClose = FIXMessage.FIXOpenCloseToOrderOpenClose(fixOpenClose);
		} catch (FieldNotFound e) {
			this.orderOpenClose = null;
		}
	}

	private void resolveOrderTIF(Message message) {
        try {
        	TimeInForce fixTimeInForce = (TimeInForce) message.getField(new TimeInForce());
        	this.orderTIF = FIXMessage.FIXTifToOrderTif(fixTimeInForce);
		} catch (FieldNotFound e) {
			this.orderTIF = null;
		}
	}

	private String resolveHeaderField(Header header, int fieldInt) {
        try {
        	String fieldVal = header.getString(fieldInt);
        	return fieldVal;
		} catch (FieldNotFound e) {
			return null;
		}
	}	

	private String resolveMessageField(Message message, int fieldInt) {
        try {
        	String fieldVal = message.getString(fieldInt);
        	return fieldVal;
		} catch (FieldNotFound e) {
			return null;
		}
	}

	private static Integer toInt(String inString) {
    	if (inString != null) {
    		return Integer.parseInt(inString);
    	}
    	return null;
	}
	
	private static Double toDouble(String inString) {
    	if (inString != null) {
    		return Double.parseDouble(inString);
    	}
    	return null;
	}

	private void resolveDirection() {
		if (messageType == MessageType.LOGON ||  messageType == MessageType.EXECUTION_REPORT || 
				messageType == MessageType.ORDER_CANCEL_REJECT) {
			this.direction = "Inbound";
		} else if (messageType == MessageType.NEW_ORDER || messageType == MessageType.REPLACE_ORDER 
				|| messageType == MessageType.CANCEL_ORDER){
    		this.direction = "Outbound";
    	} else {
    		this.direction = "Inbound";
    	}
	}

    public Message getMessage() {
        	return this.message;
        }
   
    public Message.Header getMessageHeader() {
    	return header;
    }   

    public MessageType getMessageType() {
    	return messageType;
    }

    public String getDirection() {
    	return direction;
    }

    public String getClOrdID() {
    	return clOrdID;
    }

    public String getOrigClOrdID() {
    	return origClOrdID;
    }

    public String getOrderID() {
    	return orderID;
    }

    public String getSymbol() {
    	return symbol;
    }

    public String getSymbolSfx() {
    	return symbolSfx;
    }

    public OrderSide getOrderSide() {
    	return orderSide;
    }

    public OrderStatus getOrderStatus() {
    	return orderStatus;
    }

    public OrderTIF getOrderTIF() {
    	return orderTIF;
    }

    public Integer getOrderQty() {
    	return orderQty;
    }

    public ExecutionType getExecutionType() {
    	return executionType;
    }

    public Integer getLastShares() {
    	return lastShares;
    }

    public OrderType getOrderType() {
    	return orderType;
    }

    public String getText() {
    		return text;
    }

    public Integer getCumQty() {
    	return cumQty;
    }

    public Integer getLeavesQty() {
    	return leavesQty;
    }

    public Double getAvgPx() {
    	return avgPx;
    }

    public String getSenderCompID() {
    	return senderCompID;
    }

    public String getTargetCompID() {
    	return targetCompID;
    }
    
    public Integer getMsgSeqNum() {
    	return msgSeqNum;
    }

    public String getTransactTime() {
    	return transactTime;
    }

    public String getSendingTime() {
    	return sendingTime;
    }
    
    public Double getLastPx() {
    	return lastPx;
    }

    public OrderOpenClose getOpenClose() {
    	return orderOpenClose;
    } 

	public String getDisplayID() {
		if (messageType == MessageType.EXECUTION_REPORT || messageType == MessageType.ORDER_CANCEL_REJECT) {
			return orderID;
    	} else {
    		return  "-";
    	}
    }

	public Integer getDisplayQty() {
		if (executionType == ExecutionType.PARTIAL_FILL || executionType == ExecutionType.FILL) {
			return lastShares;
    	} else {
    		return orderQty;
    	}
    }

    public String getDisplayStatus() {
		if (messageType == MessageType.EXECUTION_REPORT) {
    		return executionType.toString();
    	} else if (messageType == MessageType.ORDER_CANCEL_REJECT) {
    		return orderStatus.toString();
    	} else if (messageType == MessageType.NEW_ORDER || messageType == MessageType.REPLACE_ORDER 
				|| messageType == MessageType.CANCEL_ORDER){
    		return orderSide.toString();
    	} else {
    		return "-";
    	}
    }

}

