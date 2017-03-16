package com.roundaboutam.trader.rmp;


public class StartTime {

	static public final int RMPFieldID = 13;
	private String startTime;

	
    private StartTime(String data) {
        this.startTime = data;
    }

    public String toString() {
    	return startTime;
    }
    
    public static StartTime parse(String data) throws IllegalArgumentException {
        return new StartTime(data);
    }

}
