package com.roundaboutam.app.orders;

public class OrderIdMaker {

	private static String OrderSuffix = Long.toString(System.currentTimeMillis());
	private static int OrderNum = 0;

	public static String getNextId() {
		OrderNum = OrderNum + 1;
		return OrderSuffix + Integer.toString(OrderNum);
	}

}
