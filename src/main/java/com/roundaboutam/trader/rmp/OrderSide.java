package com.roundaboutam.trader.rmp;

import java.util.HashMap;
import java.util.Map;

public class OrderSide {
	
	static public final int RMPFieldID = 9;
	
	static private final Map<String, OrderSide> known = new HashMap<>();
	static public final OrderSide BUY = new OrderSide("BY");
	static public final OrderSide BUY_TO_COVER = new OrderSide("BTC");
    static public final OrderSide SELL = new OrderSide("SL");
    static public final OrderSide SHORT_SELL = new OrderSide("SS");

    static private final OrderSide[] array = { BUY, BUY_TO_COVER, SELL, SHORT_SELL };

	private final String name;
    
    private OrderSide(String name) {
        this.name = name;
        synchronized (OrderSide.class) {
            known.put(name, this);
        }
    }

    public String getName() {
        return name;
    }
    
    public String toString() {
        return name;
    }

    static public Object[] toArray() {
        return array;
    }

    public static OrderSide parse(String type) throws IllegalArgumentException {
        OrderSide result = known.get(type);
        if (result == null) {
            throw new IllegalArgumentException
            ("OrderSide: " + type + " is unknown.");
        }
        return result;
    }

}
