package com.tw.workshop;
/**
 * Convenience wrapper around Thread.sleep()
 * Swallows checked exceptions of Thread.sleep()
 * Provides convenient methods for randomizing sleep duration
 * @author Priyank
 *
 */
public class Sleep {

	/**
	 * Sleep for a specified milliseconds. 
	 * @param millis milliseconds to sleep
	 * @return duration slept in milliseconds
	 */
	public static long sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return millis;
	}

	/**
	 * Sleep for a random duration between minMillis and maxMillis. 
	 * @param minMillis minimum milliseconds to sleep
	 * @param maxMillis maximum milliseconds to sleep
	 * @return duration slept in milliseconds
	 * @throws IllegalArgumentException if minMillis > maxMillis
	 */
	public static long sleepRandom(long minMillis, long maxMillis) {
		if(minMillis>maxMillis) {
			throw new IllegalArgumentException("minMillis > maxMillis");
		}
		long millis = minMillis + (long) (Math.random()*(maxMillis-minMillis));
		return sleep(millis);
	}

	/**
	 * Sleep for a random duration between 0 and maxMillis milliseconds. 
	 * @param maxMillis maximum milliseconds to sleep
	 * @return duration slept in milliseconds
	 */
	public static long sleepRandom(long maxMillis) {
		return sleepRandom(0, maxMillis);
	}

}
