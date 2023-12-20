package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		assertTrue(SweBank.hasAccount("Ulrika"));
		assertTrue(SweBank.hasAccount("Bob"));
		assertTrue(Nordea.hasAccount("Bob"));
		assertTrue(DanskeBank.hasAccount("Gertrud"));
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(1000, SEK));
		SweBank.deposit("Bob", new Money(40, SEK));
		Nordea.deposit("Bob", new Money(100, SEK));
		DanskeBank.deposit("Gertrud", new Money(50, DKK));

		assertEquals(1000L, (long)SweBank.getBalance("Ulrika"));
		assertEquals(40L, (long)SweBank.getBalance("Bob"));
		assertEquals(100L, (long)Nordea.getBalance("Bob"));
		assertEquals(50L, (long)DanskeBank.getBalance("Gertrud"));
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		long ulrikaBalance = SweBank.getBalance("Ulrika");
		long bobSweBalance = SweBank.getBalance("Bob");

		Nordea.deposit("Bob", new Money(100, SEK));

		long bobNordeaBalance = Nordea.getBalance("Bob");
		long gertrudBalance = DanskeBank.getBalance("Gertrud");

		SweBank.withdraw("Ulrika", new Money(20, SEK));
		SweBank.withdraw("Bob", new Money(60, SEK));
		Nordea.withdraw("Bob", new Money(40, SEK));
		DanskeBank.withdraw("Gertrud", new Money(100, DKK));

		assertNotEquals(ulrikaBalance - 20, (long)SweBank.getBalance("Ulrika"));
		assertNotEquals(bobSweBalance - 60, (long)SweBank.getBalance("Bob"));
		assertEquals(bobNordeaBalance - 40, (long)Nordea.getBalance("Bob"));
		assertEquals(gertrudBalance, (long)DanskeBank.getBalance("Gertrud"));


	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertNotNull(SweBank.getBalance("Ulrika"));
		assertNotNull(SweBank.getBalance("Bob"));
		assertNotNull(Nordea.getBalance("Bob"));
		assertNotNull(DanskeBank.getBalance("Gertrud"));

		assertEquals(0, (long)SweBank.getBalance("Ulrika"));
		assertEquals(0, (long)SweBank.getBalance("Bob"));
		assertEquals(0, (long)Nordea.getBalance("Bob"));
		assertEquals(0, (long)DanskeBank.getBalance("Gertrud"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(50, SEK));

		assertEquals(50, (long)SweBank.getBalance("Bob"));
		assertEquals(0, (long)Nordea.getBalance("Bob"));

		SweBank.transfer("Bob", Nordea,"Bob", new Money(20, SEK));

		assertEquals(30, (long)SweBank.getBalance("Bob"));
		assertEquals(20, (long)Nordea.getBalance("Bob"));

		SweBank.transfer("Bob","Ulrika", new Money(30, SEK));

		assertEquals(0, (long)SweBank.getBalance("Bob"));
		assertEquals(30, (long)SweBank.getBalance("Ulrika"));

		SweBank.transfer("Ulrika", "Bob", new Money(40, SEK));

		assertEquals(30, (long)SweBank.getBalance("Bob"));
		assertEquals(0, (long)SweBank.getBalance("Ulrika"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(10, SEK));
		SweBank.addTimedPayment("Bob", "1", 2, 2, new Money(10, SEK), Nordea, "Bob");

		assertEquals(10L, (long)SweBank.getBalance("Bob"));
		assertEquals(0L, (long)Nordea.getBalance("Bob"));

		SweBank.tick();

		assertEquals(10L, (long)SweBank.getBalance("Bob"));
		assertEquals(0L, (long)Nordea.getBalance("Bob"));

		SweBank.tick();

		assertEquals(10L, (long)SweBank.getBalance("Bob"));
		assertEquals(0L, (long)Nordea.getBalance("Bob"));

		SweBank.tick();

		assertEquals(0, (long)SweBank.getBalance("Bob"));
		assertEquals(10L, (long)Nordea.getBalance("Bob"));
	}
}
