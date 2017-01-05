package com.roundaboutam.trader.order;

import com.roundaboutam.trader.TwoWayMap;

import quickfix.field.Side;

public class OrderSide {

	private final String name;

	// Trader Engine Types
	static public final OrderSide BUY = new OrderSide("Buy");
    static public final OrderSide SELL = new OrderSide("Sell");
    static public final OrderSide SHORT_SELL = new OrderSide("Short Sell");

    static private final OrderSide[] array = { BUY, SELL, SHORT_SELL };

    // Map QuickFIXJ Types
    static private final TwoWayMap sideMap = new TwoWayMap();

    static {
    	sideMap.put(OrderSide.BUY, new Side(Side.BUY));
    	sideMap.put(OrderSide.SELL, new Side(Side.SELL));
    	sideMap.put(OrderSide.SHORT_SELL, new Side(Side.SELL_SHORT));
    }

    private OrderSide(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static Side toFIX(OrderSide side) {
    	return (Side) sideMap.getFirst(side);
    }

    public static OrderSide fromFIX(Side side) {
    	return (OrderSide) sideMap.getSecond(side);
    }

    static public Object[] toArray() {
        return array;
    }

}
