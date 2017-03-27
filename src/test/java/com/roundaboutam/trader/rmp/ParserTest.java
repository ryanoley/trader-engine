package com.roundaboutam.trader.rmp;

import java.util.HashMap;

import com.roundaboutam.trader.rmp.Parser;

import junit.framework.TestCase;


public class ParserTest extends TestCase{

	
	public void testParserCheckString() {
		String wellFormed = "1=RMP|2=20170313-13:54:44|3=NB|4=PYSENDER|5=TRADERENGINE|6=ParseBasket";
		String badFormed = "1=RMP|2=20170313-13:54:44|3=NB4 4=PYSENDER|5=TRADERENGINE|6=ParseBasket";
		assertEquals(Parser.checkString(wellFormed), true);
		assertEquals(Parser.checkString(badFormed), false);
	}

	public void testParserGetFieldMap() {
		String wellFormed = "1=RMP|2=20170313-13:54:44|3=NB|4=PYSENDER|5=TRADERENGINE|6=ParseBasket";
		HashMap<Integer, String> outMap = new HashMap<Integer, String>();
		outMap.put(1,  "RMP");
		outMap.put(2,  "20170313-13:54:44");
		outMap.put(3,  "NB");
		outMap.put(4,  "PYSENDER");
		outMap.put(5,  "TRADERENGINE");
		outMap.put(6,  "ParseBasket");
		assertEquals(Parser.getFieldMap(wellFormed), outMap);
	}

	
}
