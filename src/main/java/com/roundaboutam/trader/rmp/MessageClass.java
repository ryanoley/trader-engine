package com.roundaboutam.trader.rmp;

import java.util.HashMap;
import java.util.Map;


public class MessageClass {

	static public final int RMPFieldID = 3;

	static private final Map<String, MessageClass> known = new HashMap<>();
    static public final MessageClass NEW_ORDER = new MessageClass("NO");
    static public final MessageClass NEW_BASKET = new MessageClass("NB");
    static public final MessageClass TO_CONSOLE = new MessageClass("C");
    static public final MessageClass BAD_RMP_SYNTAX = new MessageClass("X");
    static private final MessageClass[] array = { NEW_ORDER, NEW_BASKET, TO_CONSOLE, BAD_RMP_SYNTAX };

	private final String name;

	
    private MessageClass(String name) {
        this.name = name;
        synchronized (MessageClass.class) {
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

    public static MessageClass parse(String type) throws IllegalArgumentException {
    	MessageClass result = known.get(type.toUpperCase());
        if (result == null) {
            throw new IllegalArgumentException
            ("MessageClass: " + type + " is unknown.");
        }
        return result;
    }

}
