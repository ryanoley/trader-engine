package com.roundaboutam.trader.order;

import java.util.ArrayList;
import java.util.HashMap;



public class OrderBasketBook {

	private final HashMap<String, OrderBasket> basketMap;
	private final HashMap<String, String> nameToID;

	public OrderBasketBook() {
		basketMap = new HashMap<String, OrderBasket>();
		nameToID = new HashMap<String, String>();
	}

	public void addBasket(OrderBasket orderBasket) {
		basketMap.put(orderBasket.getBasketId(), orderBasket);
		nameToID.put(orderBasket.getBasketName(), orderBasket.getBasketId());
	}

	public OrderBasket getBasket(String basketID) {
		return basketMap.get(basketID);
	}

	public OrderBasket getBasketbyName(String basketName) {
	    String basketID = getBasketID(basketName);
	    if (basketID == null)
	    	System.out.println("Basket not found: " + basketName);
		return basketMap.get(basketID);
	}
	
	public String getBasketID(String basketName) {
		return nameToID.get(basketName);
	}
	
	public boolean basketExists(String idOrName) {
		if (basketMap.containsKey(idOrName))
			return true;
		else if (nameToID.containsKey(idOrName))
			return true;
		return false;
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

