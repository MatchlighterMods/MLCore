package ml.core.util;

import net.minecraft.item.ItemDye;
import net.minecraft.util.StatCollector;

public class StringUtils {

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
	
	public static String join(Object[] itms, String sep) {
		String out = "";

		for (int i=0; i<itms.length; i++){
			out += itms[i].toString();
			if (i<itms.length-1)
				out += sep;
			
		}
		return out;
	}
	
	public static String join(int[] itms, String sep) {
		String out = "";

		for (int i=0; i<itms.length; i++){
			out += itms[i];
			if (i<itms.length-1)
				out += sep;
			
		}
		return out;
	}
	
	public static String getLColorName(int c) {
		return StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.field_150923_a[c]);
	}
	
}
