package com.roundaboutam.trader.rmp;


public class Participation {

	static public final int RMPFieldID = 15;
	private Integer participation;


    private Participation(int data) {
        this.participation = data;
    }

    public String toString() {
    	return participation.toString();
    }

    public int getQuantity() {
    	return participation;
    }

    public String getRmpTag() {
    	return RMPFieldID + "=" + participation;
    }
    
    public static Participation parse(Integer data) throws IllegalArgumentException {
        return new Participation(data);
    }
    
    public static Participation parse(String data) throws IllegalArgumentException {
        return new Participation(Integer.parseInt(data));
    }
}
