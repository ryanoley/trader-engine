package com.roundaboutam.trader.order;

import java.util.HashMap;
import java.util.Map;

public class OrderOpenClose {
	static private final Map<String, OrderOpenClose> known = new HashMap<>();

	// Trader Engine Types
	static public final OrderOpenClose OPEN = new OrderOpenClose("Open");
    static public final OrderOpenClose CLOSE = new OrderOpenClose("Close");

    static private final OrderOpenClose[] array = { OPEN, CLOSE};

	private final String name;
    
    private OrderOpenClose(String name) {
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

    public static OrderOpenClose parse(String type) throws IllegalArgumentException {
    	OrderOpenClose result = known.get(type);
        if (result == null) {
            throw new IllegalArgumentException
            ("OrderOpenClose: " + type + " is unknown.");
        }
        return result;
    }

}
