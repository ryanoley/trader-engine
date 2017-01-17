package com.roundaboutam.trader.order;

import com.roundaboutam.trader.TwoWayMap;

import quickfix.field.OrdType;

public class OrderType {

	private final String name;

	// Trader Engine Types
    static public final OrderType MARKET = new OrderType("Market");
    static public final OrderType LIMIT = new OrderType("Limit");
    static public final OrderType MOC = new OrderType("Market On Close");
    static public final OrderType LOC = new OrderType("Limit On Close");

    static private final OrderType[] array = { MARKET, LIMIT, MOC, LOC };

    // Map QuickFIXJ Types
    static private final TwoWayMap typeMap = new TwoWayMap();

    static {
    	typeMap.put(OrderType.MARKET, new OrdType(OrdType.MARKET));
        typeMap.put(OrderType.LIMIT, new OrdType(OrdType.LIMIT));
        typeMap.put(OrderType.MOC, new OrdType(OrdType.MARKET_ON_CLOSE));
        typeMap.put(OrderType.LOC, new OrdType(OrdType.LIMIT_ON_CLOSE));
    }

    private OrderType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static OrdType toFIX(OrderType type) {
    	return (OrdType) typeMap.getFirst(type);
    }

    public static OrderType fromFIX(OrdType type) {
    	return (OrderType) typeMap.getSecond(type);
    }

    static public Object[] toArray() {
        return array;
    }

}
