package com.roundaboutam.trader.rmp;


public class TargetApp {

	static public final int RMPFieldID = 5;
	private String targetApp;

	
    private TargetApp(String data) {
        this.targetApp = data;
    }

    public String toString() {
    	return targetApp;
    }

    public String getRmpTag() {
    	return RMPFieldID + "=" + targetApp;
    }

    public static TargetApp parse(String data) throws IllegalArgumentException {
        return new TargetApp(data);
    }

}
