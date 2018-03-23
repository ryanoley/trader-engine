package com.roundaboutam.trader.ramfix;

import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.ramfix.FIXMessage;
import com.roundaboutam.trader.ramfix.MessageType;
import com.roundaboutam.trader.ramfix.OrderStatus;


import junit.framework.TestCase;



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
	}

	public void testOrdTypeMap() {	
    	assertEquals(FIXMessage.getOrdTypeMap().getFirstToSecondSize(), FIXMessage.getOrdTypeMap().getSecondToFirstSize());
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

