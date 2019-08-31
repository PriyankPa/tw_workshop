package com.tw.workshop;

public class TransactionRecord {
	
	private int amount;
	private Action action;
//	private boolean successful;

	public TransactionRecord(int amount, Action action) {
		this.amount = amount;
		this.action = action;
	}
	
	@Override
	public String toString() {
		return String.format("%s\t%d", action.name(), amount);
	}

	public int getAmount() {
		return amount;
	}

	public Action getAction() {
		return action;
	}
	
	

}
