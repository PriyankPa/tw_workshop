package com.tw.workshop;

import java.util.concurrent.ExecutionException;

import junit.framework.TestCase;

public class ProductServiceTest extends TestCase {

	public void testGetTotalPriceParallel() throws InterruptedException, ExecutionException {
		ProductService service = new ProductService();
		
		long time = System.currentTimeMillis();
		service.getTotalPriceParallel("car", "rocket").get();
		time = System.currentTimeMillis() - time;
		
		// Should take about 1000 ms to run
		assertFalse("Completed too soon: "+time, time < 995);
		assertFalse("Completed too late: "+time,time > 1050);
		
	}

	public void testGetTotalPriceSequential() throws InterruptedException, ExecutionException {
		ProductService service = new ProductService();
		
		long time = System.currentTimeMillis();
		service.getTotalPriceSequential("car", "rocket").get();
		time = System.currentTimeMillis() - time;
		
		// Should take about 2000 ms to run
		assertFalse("Completed too soon: "+time, time< 1995);
		assertFalse("Completed too late: "+time,time > 2050);
	}

}
