package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}

	@Test
	public void testGetRate() {
		float value = 0.001f;
		assertTrue(SEK.getRate() - 0.15 < value);
		assertTrue(DKK.getRate() - 0.2 < value);
		assertTrue(EUR.getRate() - 1.5 < value);
	}

	@Test
	public void testSetRate() {
		SEK.setRate(1.25);
		DKK.setRate(22.2);
		EUR.setRate(0.5);

        assertEquals(1.25, SEK.getRate(), 0.0);
        assertEquals(22.2, DKK.getRate(), 0.0);
        assertEquals(0.5, EUR.getRate(), 0.0);
	}

	@Test
	public void testGlobalValue() {
		assertEquals(15, (long)SEK.universalValue(100));
		assertEquals(150, (long)EUR.universalValue(100));
		assertEquals(20, (long)DKK.universalValue(100));
	}

	@Test
	public void testValueInThisCurrency() {
		assertEquals(1000, (long)SEK.valueInThisCurrency(100, EUR));
		assertEquals(133, (long)SEK.valueInThisCurrency(100, DKK));

		assertEquals(10, (long)EUR.valueInThisCurrency(100, SEK));
		assertEquals(13, (long)EUR.valueInThisCurrency(100, DKK));

		assertEquals(75, (long)DKK.valueInThisCurrency(100, SEK));
		assertEquals(750, (long)DKK.valueInThisCurrency(100, EUR));
	}
}
