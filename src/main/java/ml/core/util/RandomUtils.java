package ml.core.util;

import java.util.Random;

public class RandomUtils {

	public static java.util.Random rng = new java.util.Random();
	
	public static int randomInt(int max) {return rng.nextInt(max);}
	public static int randomInt(int min, int max) {return min+rng.nextInt(max-min);}
	
	public static float randomFloat() {return rng.nextFloat();}
	public static float randomFloat(float max) {return randomFloat()*max;}
	public static float randomFloat(float min, float max) {return min+randomFloat(max-min);}
	
	public static double randomDouble() {return rng.nextDouble();}
	public static double randomDouble(double max) {return randomDouble()*max;}
	public static double randomDouble(double min, double max) {return min+randomDouble(max-min);}
	
	public static boolean randomBool(double chance) {return randomDouble()<chance;}
	
	/**
	 * Gets a random number between min (inclusive) and max (exclusive) weighted toward higher numbers.<br/>
	 * e.g. If min = 0 and max = 5, the probability of 0 is 1, 1 is 2, ..., 4 is 5.
	 */
	public static int highEndWeightedInt(int min, int max, Random rand) {
		max -= min;
		int sum = 0;
		for (int i=1; i<=max; i++) {
			sum += i;
		}
		int randInt = rand.nextInt(sum+1);
		for (int i=max; i>0; i--) {
			randInt -= i;
			if (randInt <= 0) return (i + min);
		}
		return min;
	}
	
	/**
	 * Gets a random number between min (inclusive) and max (exclusive) weighted toward lower numbers.<br/>
	 * e.g. If min = 0 and max = 5, the probability of 0 is 5, 1 is 4, ..., 4 is 1.
	 */
	public static int lowEndWeightedInt(int min, int max, Random rand) {
		return max - highEndWeightedInt(min, max, rand);
	}
}
