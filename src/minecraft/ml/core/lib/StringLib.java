package ml.core.lib;

public class StringLib {

	private static String[] suffixes = {"k", "M", "G", "T", "P"};
	public static String toGroupedString(float in, int precision){
		String suffix = "";
		for (int i=suffixes.length; i>0; i--){
			if (in / Math.pow(1000, i) >= 1){
				suffix = suffixes[i-1];
				in /= Math.pow(1000, i);
				break;
			}
		}
		in = (float)(Math.round(in*Math.pow(10, precision))/Math.pow(10, precision));
		String ns = ""+in;
		return ""+(ns.replaceAll("\\.?[0]*$", ""))+suffix;
	}
	
	
}
