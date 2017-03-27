package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;


public class SymbolTest extends TestCase {

	Symbol testSymbol = Symbol.parse("IBM");
		
	public void testSymbol() {		
		assertEquals(testSymbol.toString(), "IBM");
	}

	public void testSymbolFixTags() {
		assertEquals(testSymbol.getRmpTag(), Symbol.RMPFieldID + "=IBM");
	}

}

