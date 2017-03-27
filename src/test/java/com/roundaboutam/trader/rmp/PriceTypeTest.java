package com.roundaboutam.trader.rmp;

import com.roundaboutam.trader.ramfix.FIXMessage;
import com.roundaboutam.trader.rmp.PriceType;
import quickfix.field.OrdType;

import junit.framework.TestCase;


public class PriceTypeTest extends TestCase {

	public void testPriceType() {
		assertEquals(PriceType.MARKET.toString(), "M");
		assertEquals(PriceType.LIMIT.toString(), "L");
		assertEquals(PriceType.VWAP.toString(), "V");
		assertEquals(PriceType.MARKET_ON_CLOSE.toString(), "MOC");
		assertEquals(PriceType.LIMIT_ON_CLOSE.toString(), "LOC");

		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.MARKET), new OrdType(OrdType.MARKET));
		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.LIMIT), new OrdType(OrdType.LIMIT));
		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.MARKET_ON_CLOSE), new OrdType(OrdType.MARKET_ON_CLOSE));
		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.LIMIT_ON_CLOSE), new OrdType(OrdType.LIMIT_ON_CLOSE));

		assertEquals(FIXMessage.FIXOrdTypeToPriceType(new OrdType(OrdType.MARKET)), PriceType.MARKET);
		assertEquals(FIXMessage.FIXOrdTypeToPriceType(new OrdType(OrdType.LIMIT)), PriceType.LIMIT);
		assertEquals(FIXMessage.FIXOrdTypeToPriceType(new OrdType(OrdType.MARKET_ON_CLOSE)), PriceType.MARKET_ON_CLOSE);
		assertEquals(FIXMessage.FIXOrdTypeToPriceType(new OrdType(OrdType.LIMIT_ON_CLOSE)), PriceType.LIMIT_ON_CLOSE);

		assertEquals(PriceType.toArray().length, 5);
	}

	public void testPriceTypeFixTags() {
		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.MARKET).toString(), "40=1");
		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.LIMIT).toString(), "40=2");
		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.MARKET_ON_CLOSE).toString(), "40=5");
		assertEquals(FIXMessage.priceTypeToFIXOrdType(PriceType.LIMIT_ON_CLOSE).toString(), "40=B");
	}

	public void testPriceTypeRmpTags() {
		assertEquals(PriceType.MARKET.getRmpTag(), PriceType.RMPFieldID + "=M");
		assertEquals(PriceType.LIMIT.getRmpTag(), PriceType.RMPFieldID + "=L");
		assertEquals(PriceType.VWAP.getRmpTag(), PriceType.RMPFieldID + "=V");
		assertEquals(PriceType.MARKET_ON_CLOSE.getRmpTag(), PriceType.RMPFieldID + "=MOC");
		assertEquals(PriceType.LIMIT_ON_CLOSE.getRmpTag(), PriceType.RMPFieldID + "=LOC");
	}
	
}
