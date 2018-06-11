package com.roundaboutam.trader.ramfix;


import junit.framework.TestCase;



public class TwoWayMapTest extends TestCase {

	public void testTwoWayMap() {		

		TwoWayMap testMap = new TwoWayMap();

		testMap.put("A", "B");
		assertEquals(testMap.getFirst("A"), "B");
		assertEquals(testMap.getSecond("B"), "A");

		assertEquals(testMap.getFirstToSecondSize(), 1);
		assertEquals(testMap.getSecondToFirstSize(), 1);
		testMap.put("C", "D");
		assertEquals(testMap.getFirstToSecondSize(), 2);

		testMap.put("A", "C");
		assertEquals(testMap.getFirst("A"), "C");
		assertEquals(testMap.getSecond("B"), "A");

		assertEquals(testMap.getFirstToSecondSize(), 2);
		assertEquals(testMap.getSecondToFirstSize(), 3);
	}


}
