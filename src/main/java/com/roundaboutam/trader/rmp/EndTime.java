package com.roundaboutam.trader.rmp;


public class EndTime {

	static public final int RMPFieldID = 14;
	private String endTime;

	
    private EndTime(String data) {
        this.endTime = data;
    }

    public String toString() {
    	return endTime;
    }
    
    public static EndTime parse(String data) throws IllegalArgumentException {
        return new EndTime(data);
    }

}
