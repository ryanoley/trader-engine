package com.roundaboutam.trader.rmp;

/*
 * JAVA REGEX special characters: <([{\^-=$!|]})?*+.>
 * 	\Q \E - start and end quote
 * see more (https://docs.oracle.com/javase/tutorial/essential/regex/index.html)
 */

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderBasket;
import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderTIF;



public class Parser {

	private static final String SEP = "\\|";

	public static class ParsedRMPObject {
		  public final MessageClass messageClass;
		  public final Object object;
		  public ParsedRMPObject(MessageClass messageClass, Object object) {
		    this.messageClass = messageClass;
		    this.object = object;
		  }
		}


	public static ParsedRMPObject parseMessage(String Message) {
		if (checkString(Message) == false)
			return new ParsedRMPObject(MessageClass.BAD_RMP_SYNTAX, Message);

		HashMap<Integer, String> fieldMap = getFieldMap(Message);
		MessageClass msgClass = MessageClass.parse(fieldMap.get(MessageClass.RMPFieldID));

		if (msgClass == MessageClass.TO_CONSOLE) {
			System.out.println(Message);
			return new ParsedRMPObject(msgClass, Message);
		} 
		else if (msgClass == MessageClass.NEW_BASKET) {
			return new ParsedRMPObject(msgClass, newBasket(fieldMap));
		}
		else if (msgClass == MessageClass.NEW_ORDER) {
			return new ParsedRMPObject(msgClass, newOrder(fieldMap));
		}
		else{
			System.out.println(Message);
			return new ParsedRMPObject(msgClass, Message);
		}
	
	}

	private static boolean checkString(String inString) {
		Pattern p = Pattern.compile(SEP);
		String[] items = p.split(inString);
		for(String s : items) {
        	boolean match = Pattern.matches("\\s*\\d+=\\S+\\s*", s);
            if (match==false) {
            	System.out.println("RMP - Unknown char sequence: " + s + 
            			" in message: " + inString);
            	return false;
            }
        }
		return true;
	}

	private static HashMap<Integer, String> getFieldMap(String inString) {
		HashMap<Integer, String> fieldMap = new HashMap<Integer, String>();
		Pattern p = Pattern.compile(SEP);
		String[] items = p.split(inString);

		for(String s : items) {
			Pattern p2 = Pattern.compile("\\s*(\\d+)=(\\S+)\\s*");
			Matcher matcher = p2.matcher(s);
			if (matcher.find()) {
				fieldMap.put(Integer.parseInt(matcher.group(1)), matcher.group(2));
			}
		}
		return fieldMap;
	}

	private static Order newOrder(HashMap<Integer, String> fieldMap) {
		Order order = new Order();
		PriceType priceType = PriceType.parse(fieldMap.get(PriceType.RMPFieldID));
		OrderSide orderSide = OrderSide.parse(fieldMap.get(OrderSide.RMPFieldID));
		BasketFlag basketFlag = BasketFlag.parse(fieldMap.get(BasketFlag.RMPFieldID));
		Symbol symbol = Symbol.parse(fieldMap.get(Symbol.RMPFieldID));
		Quantity quantity = Quantity.parse(fieldMap.get(Quantity.RMPFieldID));

		order.setOrderSide(orderSide);
		order.setPriceType(priceType);
		order.setSymbol(symbol.toString());
		order.setQuantity(quantity.getQuantity());

		// TODO This is a hard coded TIF as this is all that is used
		order.setOrderTIF(OrderTIF.DAY);

		// Interpret Order Side
		if (orderSide == OrderSide.BUY | orderSide == OrderSide.SHORT_SELL) {
			order.setOrderOpenClose(OrderOpenClose.OPEN);
		} else if (orderSide == OrderSide.BUY_TO_COVER) {
			order.setOrderSide(OrderSide.BUY);
			order.setOrderOpenClose(OrderOpenClose.CLOSE);
		} else if (orderSide == OrderSide.SELL) {
			order.setOrderOpenClose(OrderOpenClose.CLOSE);			
		}
		
		// Interpret PriceType
		// TODO null handling so this can be set regardless
		if (priceType == PriceType.LIMIT) {
			PriceLimit priceLimit = PriceLimit.parse(fieldMap.get(PriceLimit.RMPFieldID));
			order.setLimitPrice(priceLimit.getPriceLimit());
		} else if (priceType == PriceType.VWAP) {
			order.setVwapFlag(true);
		}

		// Get Basket Info - can this be done regardless as well?
		if (basketFlag == BasketFlag.TRUE) {
			BasketName basketName = BasketName.parse(fieldMap.get(BasketName.RMPFieldID));
			order.setOrderBasketName(basketName.toString());
		}
		
		TimeStamp timeStamp = TimeStamp.parse(fieldMap.get(TimeStamp.RMPFieldID));
		System.out.println("RMP - new order " + symbol + " at " + timeStamp);

		return order;
	}


	private static OrderBasket newBasket(HashMap<Integer, String> fieldMap) {
		BasketName basketName = BasketName.parse(fieldMap.get(BasketName.RMPFieldID));
		TimeStamp timeStamp = TimeStamp.parse(fieldMap.get(TimeStamp.RMPFieldID));
		OrderBasket orderBasket = new OrderBasket(basketName.toString());
		System.out.println("RMP - new basket " + basketName + " at " + timeStamp);
		return orderBasket;
	}

	
}



