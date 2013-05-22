package ml.core.item;

import ml.core.data.NBTUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StackUtils {

	public static NBTTagCompound getStackTag(ItemStack is) {
		if (!is.hasTagCompound()){
			is.setTagCompound(new NBTTagCompound());
		}
		return is.getTagCompound();
	}
	
	public static <T> T getTag(ItemStack is, T defaultVal, String...tagPath) {
		if (!is.hasTagCompound()) return defaultVal;
		NBTTagCompound tag = is.getTagCompound();
		return NBTUtils.getTagValue(tag, defaultVal, tagPath);
	}
	
	public static <T> void setTag(ItemStack is, T value, String... tagPath) {
		NBTUtils.setTag(getStackTag(is), value, tagPath);
	}
	
	public static boolean hasTagAt(ItemStack is, String...tagPath) {
		if (!is.hasTagCompound()) return false;
		return NBTUtils.hasTagAt(is.getTagCompound(), tagPath);
	}
}
