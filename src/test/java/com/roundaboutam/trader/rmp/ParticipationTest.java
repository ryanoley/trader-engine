package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;


public class ParticipationTest extends TestCase {

	Participation testParticipation = Participation.parse(12);
	Participation testParticipationStr = Participation.parse("13");
		
	public void testParticipation() {		
		assertEquals(testParticipation.toString(), "12");
		assertEquals(testParticipationStr.toString(), "13");
	}

	public void testParticipationFixTags() {
		assertEquals(testParticipation.getRmpTag(), Participation.RMPFieldID + "=12");
		assertEquals(testParticipationStr.getRmpTag(), Participation.RMPFieldID + "=13");
	}

}


