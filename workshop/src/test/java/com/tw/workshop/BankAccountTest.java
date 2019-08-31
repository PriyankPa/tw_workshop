package com.tw.workshop;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import junit.framework.TestCase;

/**
 * No synchronization used in BankAccount. 
 * @author Priyank
 *
 */

public class BankAccountTest extends TestCase {

	public void testDeposit() throws InterruptedException, ExecutionException {
		BankAccount account = new BankAccount();
		
		account.deposit(100)
		.thenCompose(bal -> {assertEquals(100,bal.intValue()); return account.deposit(200);})
		.thenCompose(bal -> {assertEquals(300,bal.intValue()); return account.deposit(300);})
		.thenCompose(bal -> {assertEquals(600,bal.intValue()); assertEquals(bal.intValue(), bal.intValue());return CompletableFuture.completedFuture(bal);}).get();
		
	}

	public void testWithdraw() throws InterruptedException, ExecutionException {

		BankAccount account = new BankAccount();
		
		account.deposit(1000)
		.thenCompose(bal -> {assertEquals(1000,bal.intValue()); return account.withdraw(100);})
		.thenCompose(bal -> {assertEquals(900,bal.intValue()); return account.withdraw(200);})
		.thenCompose(bal -> {assertEquals(700,bal.intValue()); return account.withdraw(800);})
		.thenCompose(bal -> {assertEquals(-100,bal.intValue()); return account.withdraw(100);})
		.thenCompose(bal -> {assertEquals(-200,bal.intValue()); return CompletableFuture.completedFuture(bal);}).get();
		
	}

	public void testGetBalance() throws InterruptedException, ExecutionException {
		BankAccount account = new BankAccount();
		assertEquals(0,account.getBalance().get().intValue());
	}
	
	public void testLargeNumberOfAsyncTransactions() throws InterruptedException, ExecutionException {
		BankAccount account = new BankAccount();
		int transactions = 10000;
		
		IntStream.range(0, transactions).forEach( i -> {account.deposit(1);});
		Sleep.sleep(2500);
		assertEquals(transactions, account.getBalance().get().intValue());
		
		IntStream.range(0, transactions).forEach( i -> {account.withdraw(1);});
		Sleep.sleep(2500);
		assertEquals(0, account.getBalance().get().intValue());
	}

}
