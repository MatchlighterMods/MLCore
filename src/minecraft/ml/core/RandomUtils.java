package ml.core;

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
}
