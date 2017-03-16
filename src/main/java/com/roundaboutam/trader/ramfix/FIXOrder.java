package com.roundaboutam.trader.ramfix;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.ReplaceOrder;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.LocateReqd;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Symbol;
import quickfix.field.SymbolSfx;
import quickfix.field.TargetSubID;
import quickfix.field.TransactTime;
import quickfix.fix42.Message;
import quickfix.fix42.NewOrderSingle;
import quickfix.fix42.OrderCancelReplaceRequest;
import quickfix.fix42.OrderCancelRequest;

public class FIXOrder {

	public static Message formatNewOrder(Order order) {
		if (order.getVwapFlag()) {
			return formatVwapOrder(order);
		} else {
			return formatNormalOrder(order);
		}
	}

	private static Message formatNormalOrder(Order order) {
		NewOrderSingle fixOrder = getNewOrderSingle(order);
		// TODO ML_SMARTDMA rejects in UAT, maybe should be used in PROD
		fixOrder.setString(TargetSubID.FIELD, "ML_ARCA");
		return fixOrder;
	}

	private static Message formatVwapOrder(Order vwapOrder) {
		// Force OrderType to Market
		vwapOrder.setPriceType(PriceType.MARKET);

		NewOrderSingle fixOrder = getNewOrderSingle(vwapOrder);

        // Destination
		fixOrder.setString(TargetSubID.FIELD, "ML_ALGO_US");

		// Current date is automatically generated for start/stop date
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    	String currentDate = dateFormat.format(new Date());

    	// Additional VWAP tags proprietary to BAML
    	String algoParams = "6401=1";
    	algoParams = algoParams + ";6403=" + vwapOrder.getParticipationRate();
    	algoParams = algoParams + ";6168=" + currentDate + "-" + vwapOrder.getStartTime();
    	algoParams = algoParams + ";126=" + currentDate + "-" + vwapOrder.getEndTime();
    	algoParams = algoParams + ";9682=v4.3.0BRT;";
    	fixOrder.setString(9999, algoParams);

		return fixOrder;

	}

	private static NewOrderSingle getNewOrderSingle(Order order) {

		NewOrderSingle fixOrder = new NewOrderSingle(
    			new ClOrdID(order.getOrderID()), 
    			new HandlInst('1'),
    			new Symbol(order.getSymbol()),
    			FIXMessage.orderSideToFIXSide(order.getOrderSide()),
    			new TransactTime(), 
    			FIXMessage.priceTypeToFIXOrdType(order.getPriceType()));

    	if (order.getSuffix() != null) {
    		fixOrder.setField(new SymbolSfx(order.getSuffix()));
    	}

    	fixOrder.setField(new OrderQty(order.getQuantity()));
    	fixOrder.setField(FIXMessage.orderTifToFIXTif(order.getOrderTIF()));
    	fixOrder.setField(FIXMessage.orderOpenCloseToFIXOpenClose(order.getOrderOpenClose()));

        if (order.getOrderSide() == OrderSide.SHORT_SELL) {
        	fixOrder.setField(new LocateReqd(false));
        	fixOrder.setString(5700, "BAML");
        }

        if (order.getPriceType() == PriceType.LIMIT) {
        	fixOrder.setField(new Price(order.getLimitPrice()));
        }
        
        return fixOrder;
	}

	public static Message formatCancelOrder(CancelOrder cancelOrder) {

		OrderCancelRequest fixOrder = new OrderCancelRequest(
	            new OrigClOrdID(cancelOrder.getOrigOrderID()), 
	            new ClOrdID(cancelOrder.getOrderID()), 
	            new Symbol(cancelOrder.getSymbol()),
	            FIXMessage.orderSideToFIXSide(cancelOrder.getOrderSide()), 
	            new TransactTime());

		fixOrder.setField(new OrderQty(cancelOrder.getQuantity()));
		
		return fixOrder;
	}

	public static Message formatReplaceOrder(ReplaceOrder replaceOrder) {

    	OrderCancelReplaceRequest fixOrder = new OrderCancelReplaceRequest(
                new OrigClOrdID(replaceOrder.getOrigOrderID()), 
                new ClOrdID(replaceOrder.getOrderID()), 
                new HandlInst('1'),
                new Symbol(replaceOrder.getSymbol()), 
                FIXMessage.orderSideToFIXSide(replaceOrder.getOrderSide()),
                new TransactTime(),
                FIXMessage.priceTypeToFIXOrdType(replaceOrder.getPriceType()));

		fixOrder.setField(new OrderQty(replaceOrder.getQuantity()));

		if (replaceOrder.getPriceType() == PriceType.LIMIT) {
			fixOrder.setField(new Price(replaceOrder.getLimitPrice()));
    	}

		return fixOrder;

	}

  
}


