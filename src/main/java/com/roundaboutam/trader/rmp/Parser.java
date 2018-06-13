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
			System.out.println("Parser - " + Message);
			return new ParsedRMPObject(msgClass, Message);
		} 
		else if (msgClass == MessageClass.NEW_BASKET) {
			return new ParsedRMPObject(msgClass, newBasket(fieldMap));
		}
		else if (msgClass == MessageClass.NEW_ORDER) {
			return new ParsedRMPObject(msgClass, newOrder(fieldMap));
		}
		else if (msgClass == MessageClass.OPEN_RMP_CONNECTION | msgClass == MessageClass.CLOSE_RMP_CONNECTION) {
			return new ParsedRMPObject(msgClass, fieldMap);
		}
		else{
			System.out.println("Parser - parseMessage SINK: " + Message);
			return new ParsedRMPObject(msgClass, Message);
		}

	}

	public static boolean checkString(String inString) {
		Pattern p = Pattern.compile(SEP);
		String[] items = p.split(inString);
		for(String s : items) {
        	boolean match = Pattern.matches("\\s*\\d+=\\S+\\s*", s);
            if (match == false) {
            	System.out.println("Parser - Unknown char sequence: " + s + " in message: " + inString);
            	return false;
            }
        }
		return true;
	}

	public static HashMap<Integer, String> getFieldMap(String inString) {
		checkString(inString);
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
		BasketName basketName = BasketName.parse(fieldMap.get(BasketName.RMPFieldID));
		Symbol symbol = Symbol.parse(fieldMap.get(Symbol.RMPFieldID));
		Quantity quantity = Quantity.parse(fieldMap.get(Quantity.RMPFieldID));

		order.setOrderSide(orderSide);
		order.setPriceType(priceType);
		order.setSymbol(symbol.toString());
		order.setQuantity(quantity.getQuantity());
		
		// Interpret Order Side
		if (orderSide == OrderSide.BUY | orderSide == OrderSide.SHORT_SELL) {
			order.setOrderOpenClose(OrderOpenClose.OPEN);
		} else if (orderSide == OrderSide.BUY_TO_COVER) {
			order.setOrderSide(OrderSide.BUY);
			order.setOrderOpenClose(OrderOpenClose.CLOSE);
		} else if (orderSide == OrderSide.SELL) {
			order.setOrderOpenClose(OrderOpenClose.CLOSE);			
		}
		
		// Interpret PriceType, set VWAP fields depending
		if (priceType == PriceType.LIMIT | priceType == PriceType.LIMIT_ON_CLOSE) {
			PriceLimit priceLimit = PriceLimit.parse(fieldMap.get(PriceLimit.RMPFieldID));
			order.setLimitPrice(priceLimit.getPriceLimit());
		} else if (priceType == PriceType.VWAP) {
			order.setVwapFlag(true);
			String startTimeString = fieldMap.get(StartTime.RMPFieldID);
			startTimeString = (startTimeString == null) ? order.getStartTime() : startTimeString;
			order.setStartTime(startTimeString);

			String endTimeString = fieldMap.get(EndTime.RMPFieldID);
			endTimeString = (endTimeString == null) ? order.getEndTime() : endTimeString;
			order.setEndTime(endTimeString);
			
			String prtString = fieldMap.get(Participation.RMPFieldID);
			Integer prtInt = (prtString == null) ? order.getParticipationRate() : Integer.parseInt(prtString);
			order.setParticipationRate(prtInt);
		}

		// Add TIF for Market and Limit orders
		if (priceType == PriceType.LIMIT | priceType == PriceType.MARKET) {
			order.setOrderTIF(OrderTIF.DAY);
		} 

		// Interpret Basket information
		if (basketName != null) {
			order.setOrderBasketName(basketName.toString());
		}

		return order;
	}


	private static OrderBasket newBasket(HashMap<Integer, String> fieldMap) {
		BasketName basketName = BasketName.parse(fieldMap.get(BasketName.RMPFieldID));
		OrderBasket orderBasket = new OrderBasket(basketName.toString());
		return orderBasket;
	}

}
