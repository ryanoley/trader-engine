package com.roundaboutam.trader.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.roundaboutam.trader.TwoWayMap;

import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.LocateReqd;
import quickfix.field.OpenClose;
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

public class FIXOrder {

    static private final TwoWayMap sideMap = new TwoWayMap();
    static private final TwoWayMap tifMap = new TwoWayMap();
    static private final TwoWayMap typeMap = new TwoWayMap();
    
	
	public static Message formatNewOrder(Order order) {
		if (VwapOrder.class.isInstance(order)) {
			return formatVwapOrder((VwapOrder) order);
		} else {
			return formatNormalOrder(order);
		}
	}

	private static Message formatNormalOrder(Order order) {
		NewOrderSingle fixOrder = getNewOrderSingle(order);		
		fixOrder.setString(TargetSubID.FIELD, "ML_ARCA");
		return fixOrder;
	}

	private static Message formatVwapOrder(VwapOrder vwapOrder) {
		// Force OrderType to Market
		vwapOrder.setOrderType(OrderType.MARKET);

		NewOrderSingle fixOrder = getNewOrderSingle(vwapOrder);

        // Destination
		fixOrder.setString(TargetSubID.FIELD, "ML_ALGO_US");

		// Current date is automatically generated for bookends
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    	String currentDate = dateFormat.format(new Date());

    	// Additional VWAP tags proprietary to BAML
    	String algoParams = "6401=1";
    	algoParams = algoParams + ";6403=" + vwapOrder.getParticipationRate();
    	algoParams = algoParams + ";6168=" + currentDate + "-" + vwapOrder.getStartTime();
    	algoParams = algoParams + ";126=" + currentDate + "-" + vwapOrder.getEndTime();
    	algoParams = algoParams + ";9682=v4.3.0BRT;";
    	fixOrder.setString(9999, algoParams);

		return fixOrder;

	}

	private static NewOrderSingle getNewOrderSingle(Order order) {

		NewOrderSingle fixOrder = new NewOrderSingle(
    			new ClOrdID(order.getOrderID()), 
    			new HandlInst('1'),
    			new Symbol(order.getSymbol()),
    			sideToFIXSide(order.getOrderSide()),
    			new TransactTime(), 
    			typeToFIXType(order.getOrderType()));

    	if (order.getSuffix() != null) {
    		fixOrder.setField(new SymbolSfx(order.getSuffix()));
    	}

    	fixOrder.setField(new OrderQty(order.getQuantity()));
    	fixOrder.setField(tifToFIXTif(order.getOrderTIF()));
    	fixOrder.setField(new OpenClose(order.getOpenClose()));

        if (order.getOrderSide() == OrderSide.SHORT_SELL) {
        	fixOrder.setField(new LocateReqd(false));
        	fixOrder.setString(5700, "BAML");
        }

        if (order.getOrderType() == OrderType.LIMIT) {
        	fixOrder.setField(new Price(order.getLimitPrice()));
        }
        
        return fixOrder;
	}

	public static Message formatCancelOrder(CancelOrder cancelOrder) {

		OrderCancelRequest fixOrder = new OrderCancelRequest(
	            new OrigClOrdID(cancelOrder.getOrigOrderID()), 
	            new ClOrdID(cancelOrder.getOrderID()), 
	            new Symbol(cancelOrder.getSymbol()),
	            sideToFIXSide(cancelOrder.getOrderSide()), 
	            new TransactTime());

		fixOrder.setField(new OrderQty(cancelOrder.getQuantity()));
		
		return fixOrder;
	}

	public static Message formatReplaceOrder(ReplaceOrder replaceOrder) {

    	OrderCancelReplaceRequest fixOrder = new OrderCancelReplaceRequest(
                new OrigClOrdID(replaceOrder.getOrigOrderID()), 
                new ClOrdID(replaceOrder.getOrderID()), 
                new HandlInst('1'),
                new Symbol(replaceOrder.getSymbol()), 
                sideToFIXSide(replaceOrder.getOrderSide()),
                new TransactTime(),
                typeToFIXType(replaceOrder.getOrderType()));

		fixOrder.setField(new OrderQty(replaceOrder.getQuantity()));

		if (replaceOrder.getOrderType() == OrderType.LIMIT) {
			fixOrder.setField(new Price(replaceOrder.getLimitPrice()));
    	}

		return fixOrder;

	}

    public static Side sideToFIXSide(OrderSide side) {
        return (Side) sideMap.getFirst(side);
    }

    public static OrderSide FIXSideToSide(Side side) {
        return (OrderSide) sideMap.getSecond(side);
    }

    public static OrdType typeToFIXType(OrderType type) {
        return (OrdType) typeMap.getFirst(type);
    }

    public static OrderType FIXTypeToType(OrdType type) {
        return (OrderType) typeMap.getSecond(type);
    }

    public static TimeInForce tifToFIXTif(OrderTIF tif) {
        return (TimeInForce) tifMap.getFirst(tif);
    }

    public static OrderTIF FIXTifToTif(TimeInForce tif) {
        return (OrderTIF) typeMap.getSecond(tif);
    }
    
    static {
    	sideMap.put(OrderSide.BUY, new Side(Side.BUY));
    	sideMap.put(OrderSide.SELL, new Side(Side.SELL));
    	sideMap.put(OrderSide.SHORT_SELL, new Side(Side.SELL_SHORT));

    	typeMap.put(OrderType.MARKET, new OrdType(OrdType.MARKET));
        typeMap.put(OrderType.LIMIT, new OrdType(OrdType.LIMIT));
        typeMap.put(OrderType.MOC, new OrdType(OrdType.MARKET_ON_CLOSE));
        typeMap.put(OrderType.LOC, new OrdType(OrdType.LIMIT_ON_CLOSE));

	    tifMap.put(OrderTIF.DAY, new TimeInForce(TimeInForce.DAY));
	    tifMap.put(OrderTIF.IOC, new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
	    tifMap.put(OrderTIF.AT_OPEN, new TimeInForce(TimeInForce.AT_THE_OPENING));
	    tifMap.put(OrderTIF.AT_CLOSE, new TimeInForce(TimeInForce.AT_THE_CLOSE));
	    tifMap.put(OrderTIF.GTC, new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));
    }

    
    
    
    
}


