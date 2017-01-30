package com.roundaboutam.trader.execution;

import com.roundaboutam.trader.order.OrderSide;

import junit.framework.TestCase;

public class ExecutionTest extends TestCase {

	public void testExecutionInit() {
		Execution execution = new Execution(
    			"1234", "1234", "BRK", "20160101-14:45:64", 
    			OrderSide.BUY, 100, 100.5);
		execution.setSuffix("B");
        assertEquals(execution.getLogEntry(), "1,1234,1234,BRK/B,20160101-14:45:64,Buy,100,100.5,0.0,0.0,null");
	}

}
