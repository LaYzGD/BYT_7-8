package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(10000L, (long)SEK100.getAmount());
		assertEquals(1000L, (long)EUR10.getAmount());
		assertEquals(20000L, (long)SEK200.getAmount());
		assertEquals(2000L, (long)EUR20.getAmount());
		assertEquals(0, (long)SEK0.getAmount());
		assertEquals(0, (long)EUR0.getAmount());
		assertEquals(-10000L, (long)SEKn100.getAmount());
	}


	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SEK100.getCurrency());
		assertEquals(EUR, EUR10.getCurrency());
		assertEquals(SEK, SEK200.getCurrency());
		assertEquals(EUR, EUR20.getCurrency());
		assertEquals(SEK, SEK0.getCurrency());
		assertEquals(EUR, EUR0.getCurrency());
		assertEquals(SEK, SEKn100.getCurrency());
	}


	@Test
	public void testToString() {
		assertEquals("100.0 SEK", SEK100.toString());
		assertEquals("10.0 EUR", EUR10.toString());
		assertEquals("200.0 SEK", SEK200.toString());
		assertEquals("20.0 EUR", EUR20.toString());
		assertEquals("0.0 SEK", SEK0.toString());
		assertEquals("0.0 EUR", EUR0.toString());
		assertEquals("-100.0 SEK", SEKn100.toString());
	}


	@Test
	public void testGlobalValue() {
		assertEquals((long)(10000*0.15), (long)SEK100.universalValue());
		assertEquals((long)(1000*1.5), (long)EUR10.universalValue());
		assertEquals((long)(20000*0.15), (long)SEK200.universalValue());
		assertEquals((long)(2000*1.5), (long)EUR20.universalValue());
		assertEquals(0, (long)SEK0.universalValue());
		assertEquals(0, (long)EUR0.universalValue());
		assertEquals((long)(-10000*0.15), (long)SEKn100.universalValue());
	}


	@Test
	public void testEqualsMoney() {
		assertTrue(EUR0.equals(new Money(0, EUR)));
		assertTrue(EUR10.equals(new Money(1000, EUR)));
		assertTrue(EUR20.equals(new Money(2000, EUR)));

		assertTrue(SEK0.equals(new Money(0, SEK)));
		assertTrue(SEK100.equals(new Money(10000, SEK)));
		assertTrue(SEK200.equals(new Money(20000, SEK)));
		assertTrue(SEKn100.equals(new Money(-10000, SEK)));

		assertTrue(EUR0.equals(SEK0));
		assertTrue(EUR10.equals(SEK100));
		assertTrue(EUR20.equals(SEK200));
	}


	@Test
	public void testAdd() {
		assertEquals(10010, (long)SEK100.add(new Money(10, SEK)).getAmount());
		assertEquals(100, (long)SEK0.add(new Money(10, EUR)).getAmount());
		assertEquals(0, (long)SEK0.add(EUR0).getAmount());
	}


	@Test
	public void testSub() {
		assertEquals(9000, (long)SEK100.sub(new Money(1000, SEK)).getAmount());
		assertEquals(0, (long)SEK0.sub(new Money(1000, EUR)).getAmount());
		assertEquals(0, (long)SEK0.sub(EUR0).getAmount());
	}


	@Test
	public void testIsZero() {
		assertTrue(SEK0.isZero());
		assertTrue(EUR0.isZero());
		assertFalse(SEK100.isZero());
		assertFalse(EUR20.isZero());
	}


	@Test
	public void testNegate() {
		assertEquals(-10000, (long)SEK100.negate().getAmount());
		assertEquals(10000, (long)SEKn100.negate().getAmount());
		assertEquals(0, (long)SEK0.negate().getAmount());
	}


	@Test
	public void testCompareTo() {
		assertEquals(0, SEK100.compareTo(EUR10));
		assertEquals(0, SEK200.compareTo(EUR20));
		assertEquals(0, SEK0.compareTo(EUR0));

		assertTrue(SEKn100.compareTo(EUR0) < 0);
		assertTrue(EUR0.compareTo(SEKn100) > 0);
	}
}
