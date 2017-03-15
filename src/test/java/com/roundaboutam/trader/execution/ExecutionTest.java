package com.roundaboutam.trader.execution;


import com.roundaboutam.trader.ramfix.ExecutionType;
import com.roundaboutam.trader.rmp.OrderSide;

import junit.framework.TestCase;

public class ExecutionTest extends TestCase {

	public void testExecutionInit() {
		Execution execution = new Execution(
    			"1234", "1234", "BRK", "20160101-14:45:64", 
    			OrderSide.BUY, 100, 100.5, ExecutionType.FILL);
		execution.setSuffix("B");
        assertEquals(execution.getLogEntry(), "1,Filled,1234,1234,BRK/B,20160101-14:45:64,BY,100,100.5,0.0,0.0,null");
	}

}
