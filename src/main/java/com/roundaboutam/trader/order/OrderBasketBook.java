package com.roundaboutam.trader.order;

import java.util.ArrayList;
import java.util.HashMap;



public class OrderBasketBook {

	private final HashMap<String, OrderBasket> basketMap;

	public OrderBasketBook() {
		basketMap = new HashMap<String, OrderBasket>();
	}

	public void addBasket(OrderBasket orderBasket) {
		basketMap.put(orderBasket.getBasketId(), orderBasket);
	}

	public OrderBasket getBasket(String basketID) {
		return basketMap.get(basketID);
	}

	public ArrayList<OrderBasket> getAllOpenBaskets() {
		ArrayList<OrderBasket> openBaskets = new ArrayList<OrderBasket>();
		for (OrderBasket ob : basketMap.values()) {
			ob.getSummary();
			if (!ob.isFilled())
				openBaskets.add(ob);
		}
		return openBaskets;
	}



}

