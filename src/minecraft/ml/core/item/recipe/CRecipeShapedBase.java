package ml.core.item.recipe;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public abstract class CRecipeShapedBase implements IRecipe {

	protected static final int MAX_CRAFT_GRID_WIDTH = 3;
	protected static final int MAX_CRAFT_GRID_HEIGHT = 3;

	protected Object[] pattern = null;
	protected int width = 0;
	protected int height = 0;
	protected boolean allowMirror = true;
	
	public CRecipeShapedBase(Object... recipe) {
		String shape = "";
		int idx = 0;

		if (recipe[idx] instanceof Boolean) {
			allowMirror = (Boolean)recipe[idx];
			if (recipe[idx+1] instanceof Object[]) {
				recipe = (Object[])recipe[idx+1];
			} else {
				idx = 1;
			}
		}

		if (recipe[idx] instanceof String[]) {
			String[] parts = ((String[])recipe[idx++]);

			for (String s : parts) {
				width = s.length();
				shape += s;
			}

			height = parts.length;
		} else {
			while (recipe[idx] instanceof String) {
				String s = (String)recipe[idx++];
				shape += s;
				width = s.length();
				height++;
			}
		}

		if (width * height != shape.length()) {
			String ret = "Invalid shaped ore recipe: ";
			for (Object tmp :  recipe) {
				ret += tmp + ", ";
			}
			throw new RuntimeException(ret);
		}

		HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

		for (; idx < recipe.length; idx += 2) {
			Character chr = (Character)recipe[idx];
			Object in = recipe[idx + 1];

			if (in instanceof ItemStack) {
				itemMap.put(chr, ((ItemStack)in).copy());
			} else if (in instanceof Item) {
				itemMap.put(chr, new ItemStack((Item)in));
			} else if (in instanceof Block) {
				itemMap.put(chr, new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
			} else if (in instanceof String) {
				itemMap.put(chr, OreDictionary.getOres((String)in));
			} else {
				String ret = "Invalid shaped ore recipe: ";
				for (Object tmp :  recipe) {
					ret += tmp + ", ";
				}
				throw new RuntimeException(ret);
			}
		}

		pattern = new Object[width * height];
		int x = 0;
		for (char chr : shape.toCharArray()) {
			pattern[x++] = itemMap.get(chr);
		}
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World par2World) {
		for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - this.width; ++x) {
			for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - this.height; ++y) {
				if (this.checkMatch(inv, x, y, true)) return true;
				if (allowMirror && this.checkMatch(inv, x, y, false)) return true;
			}
		}

		return false;
	}
	
	protected abstract boolean checkMatch(InventoryCrafting inv, int offx, int offy, boolean mirror);

	@Override
	public int getRecipeSize() {
		return width*height;
	}

}