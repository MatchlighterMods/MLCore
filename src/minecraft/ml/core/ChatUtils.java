package ml.core;

public class ChatUtils {

	public static final String special			= "\u00A7";
	
	public static final String obfuscated		= special+"k";
	public static final String bold				= special+"l";
	public static final String strikethrough	= special+"m";
	public static final String underline		= special+"n";
	public static final String italic			= special+"o";
	public static final String reset			= special+"r";
	
	public static final String[] colors = new String[]{"0","4","2","","","","","","","","","","","","","",};
	
	public static final String color_black			= special+"0";
	public static final String color_darkBlue		= special+"1";
	public static final String color_darkGreen		= special+"2";
	public static final String color_darkAqua		= special+"3";
	public static final String color_darkRed		= special+"4";
	public static final String color_darkPurple		= special+"5";
	public static final String color_gold			= special+"6";
	public static final String color_gray			= special+"7";
	public static final String color_darkGray		= special+"8";
	public static final String color_blue			= special+"9";
	public static final String color_green			= special+"a";
	public static final String color_aqua			= special+"b";
	public static final String color_red			= special+"c";
	public static final String color_lightPurple	= special+"d";
	public static final String color_yellow			= special+"e";
	public static final String color_white			= special+"f";
	
	public static final String[] nDyeColor = new String[]{
		color_black, color_darkRed, color_darkGreen, color_darkGray,
		color_darkBlue, color_darkPurple, color_darkAqua, color_gray,
		color_darkGray, color_lightPurple, color_green, color_yellow,
		color_blue, color_lightPurple, color_gold, color_white};
	
	public static String getColorStringFromDye(int dyeC) {
		return nDyeColor[dyeC];
	}
}
