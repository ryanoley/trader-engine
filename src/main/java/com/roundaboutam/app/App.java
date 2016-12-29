package com.roundaboutam.app;

import com.roundaboutam.app.orders.FixOrderFormatter;
import com.roundaboutam.app.orders.Order;
import com.roundaboutam.app.orders.OrderIdMaker;
import com.roundaboutam.app.orders.OrderSide;
import com.roundaboutam.app.orders.OrderType;

import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.fix42.NewOrderSingle;


public class App 
{
    public static void main( String[] args )
    {
    	Order vo = new Order("AAPL", OrderType.VWAP, OrderSide.BUY, 100);

    	System.out.println(vo.getOrderId());
    	System.out.println(FixOrderFormatter.getNewOrder(vo));
    	
    	vo.replaceQuantity(104);
    	System.out.println(vo.getOrderId());
    	System.out.println(FixOrderFormatter.getReplaceOrder(vo));
    	System.out.println(vo.getOrderId());
    }
    
}
