package com.roundaboutam.app.orders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.LocateReqd;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.SymbolSfx;
import quickfix.field.TargetSubID;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.fix42.NewOrderSingle;

public class FixOrderFormatter {

	public static NewOrderSingle getNewOrder(Order order) {

		// Handle Suffixes on instrumentId
		String ticker = null;
		String suffix = null;
		String[] splitId = order.getInstrumentId().split("[\\p{Punct}\\s]+");
		if (splitId.length > 1) {
			ticker = splitId[0];
			suffix = splitId[1];
		} else {
			ticker = splitId[0];
		}

		Side side = null;
		if (order.getOrderSide() == OrderSide.SHORT) {
			side = new Side('5');
		} else if (order.getOrderSide() == OrderSide.SELL) {
			side = new Side('2');
		} else if (order.getOrderSide() == OrderSide.BUY) {
			side = new Side('1');
		} else {
			// Should there be some error?
			return null;
		}

		OrdType type = null;
		if (order.getOrderType() == OrderType.MARKET) {
			type = new OrdType('1');
		} else if (order.getOrderType() == OrderType.LIMIT) {
			type = new OrdType('2');
		} else if (order.getOrderType() == OrderType.MOC) {
			type = new OrdType('5');
		} else if (order.getOrderType() == OrderType.VWAP) {
			// Vwap orders are submitted as market orders
			type = new OrdType('1');
		} else {
			// Should there be some error?
			return null;
		}

		NewOrderSingle fixOrder = new NewOrderSingle(
				new ClOrdID(Integer.toString(order.getOrderId())), 
				// Always automated execution, no broker
				new HandlInst('1'),
				new Symbol(ticker), 
				side,
				new TransactTime(), 
				type);

		// Suffixes
		if (suffix != null) {
			fixOrder.set(new SymbolSfx(suffix));
		}

		// Add additional fields as needed
		fixOrder.set(new OrderQty(order.getQuantity()));

		// Add price field for LIMIT order
		if (order.getOrderType() == OrderType.LIMIT) {
			fixOrder.set(new Price(order.getLimitPrice()));
		}

		// Always day
		fixOrder.set(new TimeInForce('0'));

		if (order.getOrderSide() == OrderSide.SHORT) {
			fixOrder.set(new LocateReqd(false));
			fixOrder.setString(5700, "BAML");
		}

		if (order.getOrderType() == OrderType.VWAP) {
			// Custom tag through this destination
			fixOrder.setString(TargetSubID.FIELD, "ML_ALGO_US");
			// This could be put into OrderConfig?
			String algoParams;
			algoParams = "6401=1";
			// Participation
			algoParams = algoParams + ";6403=0.12";
			// StartTime
			algoParams = algoParams + ";6168=" + getTradeTime(true);
			// EndTime
			algoParams = algoParams + ";126=" + getTradeTime(false);
			algoParams = algoParams + ";9682=v4.3.0BRT;";
			fixOrder.setString(9999, algoParams);

		} else {
			// This could be put into OrderConfig?
			fixOrder.setString(TargetSubID.FIELD, "ML_ARCA");
		}

		return fixOrder;
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
	
}
