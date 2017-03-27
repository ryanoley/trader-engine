package com.roundaboutam.trader.rmp;


public class Quantity {

	static public final int RMPFieldID = 10;
	private Integer quantity;


    private Quantity(int data) {
        this.quantity = data;
    }

    public String toString() {
    	return quantity.toString();
    }

    public int getQuantity() {
    	return quantity;
    }

    public String getRmpTag() {
    	return RMPFieldID + "=" + quantity;
    }

    public static Quantity parse(Integer data) throws IllegalArgumentException {
        return new Quantity(data);
    }
    
    public static Quantity parse(String data) throws IllegalArgumentException {
        return new Quantity(Integer.parseInt(data));
    }
}
