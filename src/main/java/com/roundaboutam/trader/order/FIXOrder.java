package com.roundaboutam.trader.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.roundaboutam.trader.TwoWayMap;

import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.LocateReqd;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.StopPx;
import quickfix.field.Symbol;
import quickfix.field.TargetSubID;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.fix42.Message;
import quickfix.fix42.NewOrderSingle;

public class FIXOrder {

	public static Message formatNewOrder(Order order) {

		NewOrderSingle newOrderSingle = new NewOrderSingle(
    			new ClOrdID(order.getID()), 
    			new HandlInst('1'), 
    			new Symbol(order.getSymbol()),
    			OrderSide.toFIX(order.getSide()), 
    			new TransactTime(), 
    			typeToFIXType(order.getType()));

		
		newOrderSingle.set(new OrderQty(order.getQuantity()));
    	
		return newOrderSingle;

	}

	public static Message formatReplaceOrder(Order order) {
		
	}

    public Message populateOrder(Order order, Message newOrderSingle) {
    	/*
    	 * Used to add additional flags, many required by broker
    	 */
        newOrderSingle.setField(tifToFIXTif(order.getTIF()));

        if (order.getSide() == OrderSide.SHORT_SELL
                || order.getSide() == OrderSide.SHORT_SELL_EXEMPT) {
            newOrderSingle.setField(new LocateReqd(false));
            newOrderSingle.setString(5700, "BAML");
        }

        OrderType type = order.getType();

        // CUSTOM ORDERS - Always return from within brackets
        if (type == OrderType.VWAP01) {
            // Destination
        	newOrderSingle.setString(TargetSubID.FIELD, "ML_ALGO_US");
        	// Current date is automatically generated for bookends
        	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        	String currentDate = dateFormat.format(new Date());
        	// All custom tags were requested to be submitted in this format
        	String algoParams = "6401=1";
        	algoParams = algoParams + ";6403=12"; // Percent volume of 12 percent
        	algoParams = algoParams + ";6168=" + currentDate + "-14:32:00";  // Start time
        	algoParams = algoParams + ";126=" + currentDate + "-20:58:00";  // End time
        	algoParams = algoParams + ";9682=v4.3.0BRT;";
        	newOrderSingle.setString(9999, algoParams);
        	return newOrderSingle;
        }

        // STANDARD ORDERS
        else if (type == OrderType.LIMIT) {
        	newOrderSingle.setField(new Price(order.getLimit()));
        }
        else if (type == OrderType.STOP) {
            newOrderSingle.setField(new StopPx(order.getStop()));
        } 
        else if (type == OrderType.STOP_LIMIT) {
            newOrderSingle.setField(new Price(order.getLimit()));
            newOrderSingle.setField(new StopPx(order.getStop()));
        }

        // Destination
        newOrderSingle.setString(TargetSubID.FIELD, "ML_ARCA");
        return newOrderSingle;
    }

    
    
    static private final TwoWayMap sideMap = new TwoWayMap();
    static private final TwoWayMap typeMap = new TwoWayMap();
    static private final TwoWayMap tifMap = new TwoWayMap();

    static {
        sideMap.put(OrderSide.BUY, new Side(Side.BUY));
        sideMap.put(OrderSide.SELL, new Side(Side.SELL));
        sideMap.put(OrderSide.SHORT_SELL, new Side(Side.SELL_SHORT));
        sideMap.put(OrderSide.SHORT_SELL_EXEMPT, new Side(Side.SELL_SHORT_EXEMPT));
        sideMap.put(OrderSide.CROSS, new Side(Side.CROSS));
        sideMap.put(OrderSide.CROSS_SHORT, new Side(Side.CROSS_SHORT));

        typeMap.put(OrderType.MARKET, new OrdType(OrdType.MARKET));
        typeMap.put(OrderType.LIMIT, new OrdType(OrdType.LIMIT));
        typeMap.put(OrderType.STOP, new OrdType(OrdType.STOP));
        typeMap.put(OrderType.STOP_LIMIT, new OrdType(OrdType.STOP_LIMIT));
        // CUSTOM
        typeMap.put(OrderType.VWAP01, new OrdType(OrdType.MARKET));

        tifMap.put(OrderTIF.DAY, new TimeInForce(TimeInForce.DAY));
        tifMap.put(OrderTIF.IOC, new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
        tifMap.put(OrderTIF.OPG, new TimeInForce(TimeInForce.AT_THE_OPENING));
        tifMap.put(OrderTIF.GTC, new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));
        tifMap.put(OrderTIF.GTX, new TimeInForce(TimeInForce.GOOD_TILL_CROSSING));
    }


}
