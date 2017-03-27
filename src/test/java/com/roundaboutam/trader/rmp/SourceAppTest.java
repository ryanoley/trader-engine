package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;


public class SourceAppTest extends TestCase {

	SourceApp testSourceApp = SourceApp.parse("TESTSOURCE");
		
	public void testSourceApp() {		
		assertEquals(testSourceApp.toString(), "TESTSOURCE");
	}

	public void testSourceAppFixTags() {
		assertEquals(testSourceApp.getRmpTag(), SourceApp.RMPFieldID + "=TESTSOURCE");
	}

}

