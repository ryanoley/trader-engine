package com.roundaboutam.trader.order;

public class IdGenerator {

    private static int nextID = 1;

	public static String makeID() {
		return Long.toString(System.currentTimeMillis() + (nextID++));
	}

}
