package com.tw.workshop;
/**
 * Demonstrates the performance difference between Blocking Async and Non-Blocking Async.
 * @author Priyank
 *
 */

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Blocking {
	
	public static void blockingAsyncTask(int id) {
		CompletableFuture.supplyAsync((() -> {Sleep.sleep(1000);return String.format("Blocking Task %d done", id);}),Pool.getScheduleablePool()).thenAccept(System.out::println);
	}

	public static void nonBlockingAsyncTask(int id) {
		
		Pool.getScheduleablePool().schedule(() -> System.out.println(String.format("Non Blocking Task %d done", id)), 1000,TimeUnit.MILLISECONDS);
		
	}
	
	public static void main(String[] args) {
		IntStream.range(0, 16).forEach(i -> nonBlockingAsyncTask(i));
		IntStream.range(0, 16).forEach(i -> blockingAsyncTask(i));
		
		Sleep.sleep(12000);
		
	}
	
}
