package com.roundaboutam.trader.rmp;

import java.util.HashMap;
import java.util.Map;


public class MessageClass {

	static public final int RMPFieldID = 3;

	static private final Map<String, MessageClass> known = new HashMap<>();
    static public final MessageClass OPEN_RMP_CONNECTION = new MessageClass("ORC");
    static public final MessageClass CLOSE_RMP_CONNECTION = new MessageClass("CRC");
    static public final MessageClass NEW_ORDER = new MessageClass("NO");
    static public final MessageClass NEW_BASKET = new MessageClass("NB");
    static public final MessageClass TO_CONSOLE = new MessageClass("TC");
    static public final MessageClass BAD_RMP_SYNTAX = new MessageClass("X");
    static private final MessageClass[] array = { OPEN_RMP_CONNECTION, CLOSE_RMP_CONNECTION, NEW_ORDER, NEW_BASKET, 
    		TO_CONSOLE, BAD_RMP_SYNTAX };

	private final String name;

    private MessageClass(String name) {
        this.name = name;
        synchronized (MessageClass.class) {
            known.put(name, this);
        }
    }

    public String getRmpTag() {
    	return RMPFieldID + "=" + name;
    	
    }
    
    public String toString() {
        return name;
    }

    static public Object[] toArray() {
        return array;
    }

    public static MessageClass parse(String type) throws IllegalArgumentException {
    	MessageClass result = known.get(type.toUpperCase());
        if (result == null) {
            throw new IllegalArgumentException
            ("MessageClass: " + type + " is unknown.");
        }
        return result;
    }

}
