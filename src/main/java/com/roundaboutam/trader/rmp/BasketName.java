package com.roundaboutam.trader.rmp;


public class BasketName {

	static public final int RMPFieldID = 6;
	private String basketName;

	
    private BasketName(String data) {
        this.basketName = data;
    }

    public String toString() {
    	return basketName;
    }

    public static BasketName parse(String data) throws IllegalArgumentException {
        return new BasketName(data);
    }

}
