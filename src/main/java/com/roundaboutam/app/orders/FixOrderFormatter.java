package com.roundaboutam.app.orders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.LocateReqd;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.SymbolSfx;
import quickfix.field.TargetSubID;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.fix42.Message;
import quickfix.fix42.NewOrderSingle;
import quickfix.fix42.OrderCancelReplaceRequest;
import quickfix.fix42.OrderCancelRequest;

public class FixOrderFormatter {
    
    public static OrderCancelRequest getCancelOrder(Order order) {

    	String origOrdId = order.getOrderId();
		String cancelOrdId = order.getCancelOrderId();
    	String[] ticker = parseTicker(order.getInstrumentId());
		char side = getOrderSideFixChar(order.getOrderSide());

    	OrderCancelRequest cancelOrder = new OrderCancelRequest(
                new OrigClOrdID(origOrdId), 
                new ClOrdID(cancelOrdId), 
                new Symbol(ticker[0]),
                new Side(side), 
                new TransactTime());
    	
    	cancelOrder.setField(new OrderQty(order.getQuantity()));

    	return cancelOrder;
    }
    
	public static OrderCancelReplaceRequest getReplaceOrder(Order order) {

		String origOrdId = order.getOrderId();
		String newOrdId = order.getReplaceOrderId();
		String[] ticker = parseTicker(order.getInstrumentId());
		char side = getOrderSideFixChar(order.getOrderSide());
		char type = getOrderTypeFixChar(order.getOrderType());

		OrderCancelReplaceRequest replaceOrder = new OrderCancelReplaceRequest(
				new OrigClOrdID(origOrdId), 
				new ClOrdID(newOrdId), 
				new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE), 
				new Symbol(ticker[0]),
				new Side(side),
				new TransactTime(), 
				new OrdType(type));

		addStandardTags(replaceOrder, order);

		return replaceOrder;
	}

	public static NewOrderSingle getNewOrder(Order order) {

		String[] ticker = parseTicker(order.getInstrumentId());
		char side = getOrderSideFixChar(order.getOrderSide());
		char type = getOrderTypeFixChar(order.getOrderType());

		NewOrderSingle fixOrder = new NewOrderSingle(
				new ClOrdID(order.getOrderId()), 
				new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE),
				new Symbol(ticker[0]),
				new Side(side),
				new TransactTime(), 
				new OrdType(type));

		addStandardTags(fixOrder, order);

		return fixOrder;
	}

	private static void addStandardTags(Message fixOrder, Order order) {

		String[] ticker = parseTicker(order.getInstrumentId());

		// Suffix for ticker added here
		if (ticker.length > 1) {
			fixOrder.setField(new SymbolSfx(ticker[1]));
		}

		fixOrder.setField(new OrderQty(order.getQuantity()));

		if (order.getOrderType() == OrderType.LIMIT) {
			fixOrder.setField(new Price(order.getLimitPrice()));
		}

		fixOrder.setField(new TimeInForce(TimeInForce.DAY));

		if (order.getOrderSide() == OrderSide.SHORT) {
			fixOrder.setField(new LocateReqd(false));
			fixOrder.setString(5700, "BAML");
		}

		if (order.getOrderType() == OrderType.VWAP) {
			addVwapTags(fixOrder);
		} else {
			// RealTick sends TargetSubID as destination
			fixOrder.setString(TargetSubID.FIELD, "ML_ARCA");
		}
	}

	private static void addVwapTags(Message fixOrder) {
		// All these custom tags can be found in an email from elizabeth molash at eze
		// RealTick sends TargetSubID as destination
		fixOrder.setString(TargetSubID.FIELD, "ML_ALGO_US");
		String algoParams;
		// Vwap
		algoParams = "6401=1";
		// Participation at 12%.
		algoParams = algoParams + ";6403=12";
		// StartTime
		algoParams = algoParams + ";6168=" + getTradeTime(true);
		// EndTime
		algoParams = algoParams + ";126=" + getTradeTime(false);
		algoParams = algoParams + ";9682=v4.3.0BRT;";
		// 9999 is custom tag
		fixOrder.setString(9999, algoParams);
	}

	/*
	 *  Method is used to get the UTC DateTime for a VWAP order that 
	 *  starts at 9:32 and ends at 15:58 EST
	 */
	private static String getTradeTime(boolean morningFlag) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		if (morningFlag) {
			return dateFormat.format(date) + "-14:32:00";
		} else {
			return dateFormat.format(date) + "-20:58:00";	
		}
	}

	private static char getOrderSideFixChar(OrderSide orderSide) {
		if (orderSide == OrderSide.SHORT) {
			return Side.SELL_SHORT;
		} else if (orderSide == OrderSide.SELL) {
			return Side.SELL;
		} else if (orderSide == OrderSide.BUY) {
			return Side.BUY;
		}
		return 0;
	}

	private static char getOrderTypeFixChar(OrderType orderType) {
		if (orderType == OrderType.MARKET) {
			return OrdType.MARKET;
		} else if (orderType == OrderType.LIMIT) {
			return OrdType.LIMIT;
		} else if (orderType == OrderType.MOC) {
			return OrdType.MARKET_ON_CLOSE;
		} else if (orderType == OrderType.VWAP) {
			return OrdType.MARKET;
		}
		return 0;
	}

	private static String[] parseTicker(String instrumentId) {
		return instrumentId.split("[\\p{Punct}\\s]+");
	}

}
