package com.roundaboutam.trader.rmp;


public class TimeStamp {

	static public final int RMPFieldID = 2;
	private String timeStamp;

	
    private TimeStamp(String data) {
        this.timeStamp = data;
    }

    public String toString() {
    	return timeStamp;
    }

    public String getRmpTag() {
    	return RMPFieldID + "=" + timeStamp;
    }

    public static TimeStamp parse(String data) throws IllegalArgumentException {
        return new TimeStamp(data);
    }

}
