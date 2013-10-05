package ml.core;

import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {
	
	public static final EnumChatFormatting[] nDyeColor = new EnumChatFormatting[]{
		EnumChatFormatting.BLACK, EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GOLD,
		EnumChatFormatting.DARK_BLUE, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.GRAY,
		EnumChatFormatting.DARK_GRAY, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.GREEN, EnumChatFormatting.YELLOW,
		EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.GOLD, EnumChatFormatting.WHITE,
	};
	
	public static String getColorStringFromDye(int dyeC) {
		return nDyeColor[dyeC].toString();
	}
}
