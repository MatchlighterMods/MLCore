package ml.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DyeUtils {

	/**
	 * Takes an ItemStack of OreDictionary dye and returns the vanilla Dye Id for that color
	 */
	public static int getVanillaColorId(ItemStack mOre) {
		if (mOre == null) return -1;

		for (int i=0; i<16; i++){
			if (OreDictionary.itemMatches(new ItemStack(Items.dye, 1, i), mOre, true)){
				return i;
			}
		}
		return -1;
	}
	
	public static int getDyeColor(ItemStack dyeStack) {
		int dyeId = getVanillaColorId(dyeStack);
		if (dyeId > -1) {
			return ItemDye.field_150922_c[dyeId];
		}
		return -1;
	}
	
	public static boolean isDye(ItemStack stack) {
		return getVanillaColorId(stack) > -1;
	}
	
	public static int mixDyeColors(int baseColor, Collection<ItemStack> dyes) {
		int r=0, g=0, b=0;
		int count = 0;
		if (baseColor > -1) {
			r += (baseColor >> 16);
			g += (baseColor >> 8) & 0xFF;
			b += (baseColor & 0xFF);
			count++;
		}
		for (ItemStack ds : dyes) {
			int dcolor = getDyeColor(ds);
			if (dcolor < 0) continue;
			r += (dcolor >> 16);
			g += (dcolor >> 8) & 0xFF;
			b += (dcolor & 0xFF);
			count++;
		}
		r /= count;
		g /= count;
		b /= count;
		return (r<<16) | (g<<8) | b;
	}
	
	public static int mixDyeColors(Collection<ItemStack> dyes) {
		return mixDyeColors(-1, dyes);
	}
	
	private static final String[] dyes =
        {
            "Black",
            "Red",
            "Green",
            "Brown",
            "Blue",
            "Purple",
            "Cyan",
            "LightGray",
            "Gray",
            "Pink",
            "Lime",
            "Yellow",
            "LightBlue",
            "Magenta",
            "Orange",
            "White"
        };
	public static List<ItemStack> getAllDyeStacks() {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for (int i=0; i<ItemDye.field_150922_c.length; i++) {
			stacks.addAll(OreDictionary.getOres("dye"+dyes[i]));
		}
		return stacks;
	}
}
