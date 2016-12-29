package com.roundaboutam.app;

import com.roundaboutam.app.orders.FixOrderFormatter;
import com.roundaboutam.app.orders.Order;
import com.roundaboutam.app.orders.OrderSide;
import com.roundaboutam.app.orders.OrderType;

import quickfix.fix42.NewOrderSingle;


public class App 
{
    public static void main( String[] args )
    {
    	Order vo = new Order(123, "AAPL", OrderType.VWAP, OrderSide.BUY, 100);
    	System.out.println("Order created for: " + vo.getInstrumentId());
    	NewOrderSingle no = FixOrderFormatter.getNewOrder(vo);
    	System.out.println(no.toString());
    }
}
