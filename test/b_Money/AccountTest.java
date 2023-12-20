package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("1", 2, 2, new Money(100, SEK), SweBank, "Alice");
		testAccount.removeTimedPayment("1");
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("1", 2, 2, new Money(10, SEK), SweBank, "Alice");

		assertEquals(10000000, (long)testAccount.getBalance().getAmount());
		assertEquals(1000000, (long)SweBank.getBalance("Alice"));

		testAccount.tick();

		assertEquals(10000000, (long)testAccount.getBalance().getAmount());
		assertEquals(1000000, (long)SweBank.getBalance("Alice"));

		testAccount.tick();

		assertEquals(10000000, (long)testAccount.getBalance().getAmount());
		assertEquals(1000000, (long)SweBank.getBalance("Alice"));

		testAccount.tick();

		assertEquals(10000000 - 10, (long)testAccount.getBalance().getAmount());
		assertEquals(1000010, (long)SweBank.getBalance("Alice"));
		testAccount.removeTimedPayment("1");

		testAccount.addTimedPayment("2", -1, -1, new Money(10, SEK), SweBank, "Alice");

		testAccount.tick();
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();

		assertEquals(10000000 - 10, (long)testAccount.getBalance().getAmount());
		assertEquals(1000010, (long)SweBank.getBalance("Alice"));

		testAccount.addTimedPayment("3", 2, 2, new Money(1000, SEK), SweBank, "Alice");
		testAccount.withdraw(new Money(100000000, SEK));
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();
		assertEquals(0, (long)testAccount.getBalance().getAmount());
		assertEquals(1000010, (long)SweBank.getBalance("Alice"));
		testAccount.removeTimedPayment("3");
		testAccount.deposit(new Money(1000010, SEK));
		assertEquals(1000010, (long)testAccount.getBalance().getAmount());

		testAccount.addTimedPayment("4", 2, 2, new Money(1000011, SEK), SweBank, "Alice");

		testAccount.tick();
		testAccount.tick();
		testAccount.tick();

		assertEquals(1000010, (long)testAccount.getBalance().getAmount());
		assertEquals(1000010, (long)SweBank.getBalance("Alice"));
		testAccount.removeTimedPayment("4");
	}

	@Test
	public void testAddWithdraw() {
		assertEquals(10000000, (long)testAccount.getBalance().getAmount());

		testAccount.withdraw(new Money(100, SEK));
		assertEquals(10000000 - 100, (long)testAccount.getBalance().getAmount());

		testAccount.withdraw(new Money(100000000, SEK));
		assertEquals(0, (long)testAccount.getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() {
		assertEquals(10000000, (long)testAccount.getBalance().getAmount());
		assertEquals(SEK, testAccount.getBalance().getCurrency());
	}
}
