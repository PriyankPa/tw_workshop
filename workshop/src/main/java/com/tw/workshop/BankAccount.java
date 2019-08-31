package com.tw.workshop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Stores state of a bank account. 
 * Supports Deposit, Withdraw and Check balance operations. 
 * Negative balance permitted. 
 * 
 * 
 * Exercise Objectives: 
 * 1. Achieve data integrity in asynchronous code using a single-threaded event loop. Eliminate the requirement of locking/synchronization in order to improve system efficiency and throughput.
 * 2. Improve Execution Time: Decompose activity into IO-bound and CPU bound tasks. Run IO Bound / Blocking tasks on a large thread pool run CPU bound tasks on a single thread. 
 * 
 * @author Priyank
 *
 */
public class BankAccount {

	private int balance;
	
	private List<TransactionRecord> transactions = new ArrayList<TransactionRecord>();
	
	public CompletableFuture<Integer> deposit(int amount) {
		return CompletableFuture
				.supplyAsync(() -> {return RbiService.doNotify(this, Action.DEPOSIT, amount);},Pool.getIoBound())
				.thenApplyAsync(result -> { if(result) {balance += amount; transactions.add(new TransactionRecord(amount, Action.DEPOSIT));} return balance;},Pool.getCpuBound());
	}
	
	public CompletableFuture<Integer> withdraw(int amount) {
		return CompletableFuture
				.supplyAsync(() -> {return RbiService.doNotify(this, Action.WITHDRAW, amount);},Pool.getIoBound())
				.thenApplyAsync(result -> { if(result) {balance -= amount; transactions.add(new TransactionRecord(amount, Action.DEPOSIT));} return balance;},Pool.getCpuBound());
	}
	
	//It is necessary to use single threaded pool so as to serialize access to balance. Parallel access may lead to dirty reads.
	public CompletableFuture<Integer>  getBalance() {
		return CompletableFuture
				.supplyAsync((() -> balance),Pool.getCpuBound());
	}
	
	private int calculateBalance() {
		return transactions.stream().mapToInt(t -> { return (t.getAction()==Action.DEPOSIT?1:-1)*t.getAmount();}).sum();
	}
	
	public CompletableFuture<Integer> getCalculatedBalance() {
		return CompletableFuture
				.supplyAsync(() -> {return calculateBalance();},Pool.getCpuBound());
	}
	
	
		
	
	
	
}
