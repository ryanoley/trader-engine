package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;


public class EndTimeTest extends TestCase {

	EndTime testEndTime = EndTime.parse("20170324-15:24:33:010");
		
	public void testEndTime() {		
		assertEquals(testEndTime.toString(), "20170324-15:24:33:010");
	}

	public void testEndTimeRmpTags() {
		assertEquals(testEndTime.getRmpTag(), EndTime.RMPFieldID + "=20170324-15:24:33:010");
	}

}

