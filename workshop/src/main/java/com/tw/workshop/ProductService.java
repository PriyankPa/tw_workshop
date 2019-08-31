package com.tw.workshop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates sequencing of a set of asynchronous tasks to obtain consistent results irrespective of the time taken to complete each asynchronous operation.
 * Obtain Price of two products sequentially and calculate the total price
 * Obtain Price of two products parallelly and calculate the total price
 * @author Priyank
 *
 */
public class ProductService {

	Map<String, Integer> priceMap;
	
	public ProductService() {
		priceMap = new HashMap<String, Integer>();
		priceMap.put("car", 100);
		priceMap.put("rocket", 800);
	}

	private Integer getPrice(String id) {
		Sleep.sleep(1000); // Change by 100ms +/- to fail tests 
		return priceMap.get(id);
	}

	public CompletableFuture<Integer> getTotalPriceParallel(String id1, String id2) {
		
		System.out.println("Get Total Price Parallel");
		
		CompletableFuture<Integer> cf1 = CompletableFuture
				.supplyAsync((() -> {return  getPrice(id1);}),Pool.getIoBound())
				.thenApplyAsync( x -> {System.out.println("Price ("+ id1 + "): "+x); return x;},Pool.getCpuBound());

		CompletableFuture<Integer> cf2 = CompletableFuture
				.supplyAsync((() -> {return  getPrice(id2);}),Pool.getIoBound())
				.thenApplyAsync( x -> {System.out.println("Price ("+ id2 + "): "+x); return x;},Pool.getCpuBound());
		
		CompletableFuture<Integer> cf = cf1.thenCombine(cf2, (a,b) -> {System.out.println("Total Price : "+(a+b));return a+b;});
		
		return cf;

	}
	
	public CompletableFuture<Integer> getTotalPriceSequential(String id1, String id2) {
		
		System.out.println("Get Total Price Sequential");

		return CompletableFuture
				.supplyAsync((() -> {return  getPrice(id1);}),Pool.getIoBound())
				.thenApplyAsync( x -> {System.out.println("Cumilative Price after ("+ id1 + "): "+x); return x;},Pool.getCpuBound())
				.thenApplyAsync((x -> {return  x + getPrice(id2);}),Pool.getIoBound())
				.thenApplyAsync( x -> {System.out.println("Cumilative Price after ("+ id2 + "): "+x); return x;},Pool.getCpuBound());


	}


	public static void main(String[] args) {
		final ProductService p = new ProductService();

		long ts = System.currentTimeMillis();
		
		System.out.println("Begin: In main");
		
		p.getTotalPriceParallel("car", "rocket");
		p.getTotalPriceSequential("car", "rocket");

		System.out.println("End: Back to main in " + (System.currentTimeMillis() - ts)+ " ms");


		try {
			
			Thread.sleep(5000);
			
			Pool.getIoBound().shutdown();
			Pool.getIoBound().awaitTermination(5000, TimeUnit.MILLISECONDS);
			
			Pool.getCpuBound().shutdown();
			Pool.getCpuBound().awaitTermination(5000, TimeUnit.MILLISECONDS);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Main Thread Exit in: " + (System.currentTimeMillis() - ts)+ " ms");



	}
}
