package com.tw.workshop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Pool {
	//Since we are not using synchronization, single threaded execution is required for operations that would otherwise require synchronization.
	private static ExecutorService cpuBound = Executors.newSingleThreadExecutor();
	private static ExecutorService ioBound = Executors.newFixedThreadPool(1000);
	private static ScheduledExecutorService scheduleablePool = Executors.newScheduledThreadPool(4);
	
	public static ExecutorService getCpuBound() {
		return cpuBound;
	}
	public static ExecutorService getIoBound() {
		return ioBound;
	}
	public static ScheduledExecutorService getScheduleablePool() {
		return scheduleablePool;
	}
	
}
