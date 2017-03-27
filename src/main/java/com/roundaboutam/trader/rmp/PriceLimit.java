package com.roundaboutam.trader.rmp;


public class PriceLimit {

	static public final int RMPFieldID = 12;
	private Double priceLimit;


    private PriceLimit(Double data) {
        this.priceLimit = data;
    }

    public String toString() {
    	return priceLimit.toString();
    }

    public Double getPriceLimit() {
    	return priceLimit;
    }

    public String getRmpTag() {
    	return RMPFieldID + "=" + priceLimit;    	
    }

    public static PriceLimit parse(Double data) throws IllegalArgumentException {
        return new PriceLimit(data);
    }

    public static PriceLimit parse(String data) throws IllegalArgumentException {
        return new PriceLimit(Double.parseDouble(data));
    }

}
