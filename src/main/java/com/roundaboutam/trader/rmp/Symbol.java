package com.roundaboutam.trader.rmp;


public class Symbol {

	static public final int RMPFieldID = 7;
	private String symbol;


    private Symbol(String data) {
        this.symbol = data;
    }

    public String toString() {
    	return symbol;
    }

    public String getRmpTag() {
    	return RMPFieldID + "=" + symbol;
    }

    public static Symbol parse(String data) throws IllegalArgumentException {
        return new Symbol(data);
    }

}
