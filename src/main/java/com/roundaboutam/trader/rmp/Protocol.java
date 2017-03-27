package com.roundaboutam.trader.rmp;

import java.util.HashMap;
import java.util.Map;

public class Protocol {

	static public final int RMPFieldID = 1;
	
	static private final Map<String, Protocol> known = new HashMap<>();
    static public final Protocol RMP = new Protocol("RMP");
    static private final Protocol[] array = { RMP };

	private final String name;


    private Protocol(String name) {
        this.name = name;
        synchronized (Protocol.class) {
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

    public String getRmpTag() {
    	return RMPFieldID + "=" + name;
    }

    public static Protocol parse(String type) throws IllegalArgumentException {
    	Protocol result = known.get(type.toUpperCase());
        if (result == null) {
            throw new IllegalArgumentException
            ("Protocol: " + type + " is unknown.");
        }
        return result;
    }

}
