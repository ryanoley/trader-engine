package com.roundaboutam.trader.ramfix;

import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.ramfix.FIXMessage;
import com.roundaboutam.trader.ramfix.MessageType;
import com.roundaboutam.trader.ramfix.OrderStatus;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import junit.framework.TestCase;
import quickfix.field.ExecType;
import quickfix.field.MsgType;
import quickfix.field.OpenClose;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;



public class FIXMessageTest extends TestCase {

	public void testMsgTypeMap() {    	
    	assertEquals(FIXMessage.getMsgTypeMap().getFirstToSecondSize(), FIXMessage.getMsgTypeMap().getSecondToFirstSize());
    	assertEquals(FIXMessage.getMsgTypeMap().getFirstToSecondSize(), MessageType.toArray().length);
	}

	public void testExecTypeMap() {
    	assertEquals(FIXMessage.getExecTypeMap().getFirstToSecondSize(), FIXMessage.getExecTypeMap().getSecondToFirstSize());
    	assertEquals(FIXMessage.getExecTypeMap().getFirstToSecondSize(), ExecutionType.toArray().length);
	}


	public void testOrdStatusMap() {
    	assertEquals(FIXMessage.getOrdStatusMap().getFirstToSecondSize(), FIXMessage.getOrdStatusMap().getSecondToFirstSize());
    	assertEquals(FIXMessage.getOrdStatusMap().getFirstToSecondSize(), OrderStatus.toArray().length);
	}

	public void testSideMap() {
    	assertEquals(FIXMessage.getSideMap().getFirstToSecondSize(), FIXMessage.getSideMap().getSecondToFirstSize());
    	assertEquals(FIXMessage.getSideMap().getFirstToSecondSize(), OrderSide.toArray().length);
	}

	public void testOrdTypeMap() {	
    	assertEquals(FIXMessage.getOrdTypeMap().getFirstToSecondSize(), FIXMessage.getOrdTypeMap().getSecondToFirstSize());
    	assertEquals(FIXMessage.getOrdTypeMap().getFirstToSecondSize(), PriceType.toArray().length);
	}

	public void testTimeInForceMap() {
    	assertEquals(FIXMessage.getTifMap().getFirstToSecondSize(), FIXMessage.getTifMap().getSecondToFirstSize());
    	assertEquals(FIXMessage.getTifMap().getFirstToSecondSize(), OrderTIF.toArray().length);
	}


	public void testOpenCloseMap() {
    	assertEquals(FIXMessage.getOpenCloseMap().getFirstToSecondSize(), FIXMessage.getOpenCloseMap().getSecondToFirstSize());
    	assertEquals(FIXMessage.getOpenCloseMap().getFirstToSecondSize(), OrderOpenClose.toArray().length);
	}


}

