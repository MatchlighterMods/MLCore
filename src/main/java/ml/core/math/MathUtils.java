package ml.core.math;

public class MathUtils {

	/**
	 * @return a<=x<=b
	 */
	public static boolean between(double a, double x, double b){
		return a <= x && x <= b;
	}
}
