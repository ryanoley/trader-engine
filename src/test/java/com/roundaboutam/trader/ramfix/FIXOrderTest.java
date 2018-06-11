package com.roundaboutam.trader.ramfix;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.ReplaceOrder;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import junit.framework.TestCase;
import quickfix.fix42.Message;
import quickfix.FieldNotFound;
import quickfix.SessionID;
import quickfix.field.MsgType;
import quickfix.field.OpenClose;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TargetSubID;
import quickfix.field.TimeInForce;



public class FIXOrderTest extends TestCase {

    private Order getCleanOrder() {
        Order order = new Order();
		order.setSymbol("SPY");
	    order.setQuantity(500);
		order.setOrderSide(OrderSide.BUY);
        order.setPriceType(PriceType.LIMIT);
        order.setOrderTIF(OrderTIF.DAY);
        order.setLimitPrice(100.0);
        order.setOrderOpenClose(OrderOpenClose.OPEN);
        order.setSessionID(new SessionID("FIX.4.2:ROUNDTEST02->REALTICK2:RYAN"));
        return order;
    }
    

	public void testNewOrder() throws FieldNotFound {
		Order order1 = getCleanOrder();
		Message message = FIXOrder.formatNewOrder(order1, true);
		assertEquals(message.getHeader().getField(new MsgType()).getValue(), MsgType.ORDER_SINGLE);
		assertEquals(message.getField(new Symbol()).getValue(), "SPY");
		assertEquals(message.getField(new OrderQty()).getValue(), 500.);
		assertEquals(message.getField(new Side()).getValue(), Side.BUY);
		assertEquals(message.getField(new OrdType()).getValue(), OrdType.LIMIT);
		assertEquals(message.getField(new TimeInForce()).getValue(), TimeInForce.DAY);
		assertEquals(message.getField(new Price()).getValue(), 100.);
		assertEquals(message.getField(new OpenClose()).getValue(), OpenClose.OPEN);
		assertEquals(message.getField(new TargetSubID()).getValue(), "ML_SMARTDMA");
		
	}

	public void testNewVwapOrder() throws FieldNotFound {
		Order order1 = getCleanOrder();
        order1.setPriceType(PriceType.MARKET);
		order1.setVwapFlag(true);
		Message message = FIXOrder.formatNewOrder(order1, true);
		assertEquals(message.getHeader().getField(new MsgType()).getValue(), MsgType.ORDER_SINGLE);
		assertEquals(message.getField(new Symbol()).getValue(), "SPY");
		assertEquals(message.getField(new OrderQty()).getValue(), 500.);
		assertEquals(message.getField(new Side()).getValue(), Side.BUY);
		assertEquals(message.getField(new OrdType()).getValue(), OrdType.MARKET);
		assertEquals(message.getField(new TimeInForce()).getValue(), TimeInForce.DAY);
		assertEquals(message.getField(new OpenClose()).getValue(), OpenClose.OPEN);
		assertEquals(message.getField(new TargetSubID()).getValue(), "ML_ALGO_US");
	}

	public void testFormatCancelOrder() throws FieldNotFound {
		Order order1 = getCleanOrder();
		CancelOrder cancelOrder = new CancelOrder(order1);
		Message message = FIXOrder.formatCancelOrder(cancelOrder);
		assertEquals(message.getHeader().getField(new MsgType()).getValue(), MsgType.ORDER_CANCEL_REQUEST);
		assertEquals(message.getField(new Symbol()).getValue(), "SPY");
		assertEquals(message.getField(new OrderQty()).getValue(), 500.);
		assertEquals(message.getField(new Side()).getValue(), Side.BUY);
		assertEquals(message.getField(new OrigClOrdID()).getValue(), order1.getOrderID());
	}

	public void testFormatReplaceOrder() throws FieldNotFound {
		Order order1 = getCleanOrder();
		ReplaceOrder replaceOrder = new ReplaceOrder(order1);
		replaceOrder.setQuantity(1000);
		
		Message message = FIXOrder.formatReplaceOrder(replaceOrder);
		assertEquals(message.getHeader().getField(new MsgType()).getValue(), MsgType.ORDER_CANCEL_REPLACE_REQUEST);
		assertEquals(message.getField(new Symbol()).getValue(), "SPY");
		assertEquals(message.getField(new OrderQty()).getValue(), 1000.);
		assertEquals(message.getField(new Side()).getValue(), Side.BUY);
		assertEquals(message.getField(new OrdType()).getValue(), OrdType.LIMIT);
		assertEquals(message.getField(new OrigClOrdID()).getValue(), order1.getOrderID());
	}
	

	public void testgetUTCDateTimeString() throws FieldNotFound {
		TimeZone tzLocal = TimeZone.getTimeZone("America/New_York");
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setTimeZone(tzLocal);
		String todayString = dateFormat.format(new Date());
		
		String inpTime = "09:30:00";
		String utcTime = FIXOrder.getUTCDateTimeString(inpTime);
		
		boolean inDST = TimeZone.getTimeZone("America/New_York").inDaylightTime(new Date());
		if(inDST)
			assertEquals(utcTime, todayString + "-13:30:00");
		else
			assertEquals(utcTime, todayString + "-14:30:00");
	}
		
}






