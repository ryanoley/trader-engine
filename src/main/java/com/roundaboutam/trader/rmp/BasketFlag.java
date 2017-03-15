package com.roundaboutam.trader.rmp;

import java.util.HashMap;
import java.util.Map;


public class BasketFlag {

	static public final int RMPFieldID = 8;

	static private final Map<String, BasketFlag> known = new HashMap<>();
    static public final BasketFlag TRUE = new BasketFlag("T");
    static public final BasketFlag FALSE = new BasketFlag("F");
    static private final BasketFlag[] array = { TRUE, FALSE };
 
	private final String name;

    private BasketFlag(String flag) {
        this.name = flag;
        synchronized (BasketFlag.class) {
            known.put(flag, this);
        }
    }

    public String toString() {
        return name;
    }

    static public Object[] toArray() {
        return array;
    }

    public static BasketFlag parse(String flag) throws IllegalArgumentException {
    	BasketFlag result = known.get(flag.toUpperCase());
        if (result == null) {
            throw new IllegalArgumentException
            ("BasketFlag: " + flag + " is unknown.");
        }
        return result;
    }

}
