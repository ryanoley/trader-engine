package com.roundaboutam.trader.rmp;

import java.util.HashMap;
import java.util.Map;

import com.roundaboutam.trader.rmp.PriceType;


public class PriceType {

	static public final int RMPFieldID = 11;

	static private final Map<String, PriceType> known = new HashMap<>();
    static public final PriceType MARKET = new PriceType("M");
    static public final PriceType LIMIT = new PriceType("L");
    static public final PriceType VWAP = new PriceType("V");
    static public final PriceType MARKET_ON_CLOSE = new PriceType("MOC");
    static public final PriceType LIMIT_ON_CLOSE = new PriceType("LOC");
    static private final PriceType[] array = { MARKET, LIMIT, VWAP, MARKET_ON_CLOSE, LIMIT_ON_CLOSE };

	private final String name;


    private PriceType(String type) {
        this.name = type;
        synchronized (PriceType.class) {
            known.put(type, this);
        }
    }

    public String toString() {
        return name;
    }

    static public Object[] toArray() {
        return array;
    }

    public static PriceType parse(String type) throws IllegalArgumentException {
    	PriceType result = known.get(type.toUpperCase());
        if (result == null) {
            throw new IllegalArgumentException
            ("PriceType: " + type + " is unknown.");
        }
        return result;
    }

}
