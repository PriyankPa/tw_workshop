package com.tw.workshop;

public class RbiService {

	public static boolean doNotify(BankAccount account, Action action, int amount) {
		Sleep.sleepRandom(100);
		return true;
	}
}
