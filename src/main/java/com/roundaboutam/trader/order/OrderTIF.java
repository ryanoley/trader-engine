package com.roundaboutam.trader.order;

import com.roundaboutam.trader.TwoWayMap;

import quickfix.field.TimeInForce;

public class OrderTIF {

    private final String name;

	// Trader Engine Types
    static public final OrderTIF DAY = new OrderTIF("Day");
    static public final OrderTIF IOC = new OrderTIF("Immediate Or Cancel");
    static public final OrderTIF AT_OPEN = new OrderTIF("At Open");
    static public final OrderTIF AT_CLOSE = new OrderTIF("At Close");
    static public final OrderTIF GTC = new OrderTIF("Good Till Cancelled");

    static private final OrderTIF[] array = { DAY, IOC, AT_OPEN, AT_CLOSE, GTC };

    // Map QuickFIXJ Types
    static private final TwoWayMap tifMap = new TwoWayMap();

    static {
	    tifMap.put(OrderTIF.DAY, new TimeInForce(TimeInForce.DAY));
	    tifMap.put(OrderTIF.IOC, new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
	    tifMap.put(OrderTIF.AT_OPEN, new TimeInForce(TimeInForce.AT_THE_OPENING));
	    tifMap.put(OrderTIF.AT_CLOSE, new TimeInForce(TimeInForce.AT_THE_CLOSE));
	    tifMap.put(OrderTIF.GTC, new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));
    }

    private OrderTIF(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static TimeInForce toFIX(OrderTIF tif) {
    	return (TimeInForce) tifMap.getFirst(tif);
    }

    public static OrderTIF fromFIX(TimeInForce tif) {
    	return (OrderTIF) tifMap.getSecond(tif);
    }

    static public Object[] toArray() {
        return array;
    }

}
