package com.roundaboutam.trader.rmp;


public class SourceApp {

	static public final int RMPFieldID = 4;
	private String sendingApp;

	
    private SourceApp(String data) {
        this.sendingApp = data;
    }

    public String toString() {
    	return sendingApp;
    }

    public static SourceApp parse(String data) throws IllegalArgumentException {
        return new SourceApp(data);
    }

}
