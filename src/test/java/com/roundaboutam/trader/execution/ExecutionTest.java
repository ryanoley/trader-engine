package com.roundaboutam.trader.execution;


import junit.framework.TestCase;

public class ExecutionTest extends TestCase {

	public void testExecutionInit() {
		Execution execution = new Execution(
    			"1234", "1234", "BRK", "20160101-14:45:64", 
    			"Buy", 100, 100.5, Execution.FILL);
		execution.setSuffix("B");
        assertEquals(execution.getLogEntry(), "1,F,1234,1234,BRK/B,20160101-14:45:64,Buy,100,100.5,0.0,0.0,null");
	}

}
