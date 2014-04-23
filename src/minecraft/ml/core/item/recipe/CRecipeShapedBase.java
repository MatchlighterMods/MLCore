package ml.core.item.recipe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;

import ml.core.item.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public abstract class CRecipeShapedBase implements IRecipe {

	public static final int MAX_CRAFT_GRID_WIDTH = 3;
	public static final int MAX_CRAFT_GRID_HEIGHT = 3;

	protected Object[] pattern = null;
	protected int width = 0;
	protected int height = 0;
	protected boolean allowMirror = false;
	
	public CRecipeShapedBase(int w, int h) {
		width = w;
		height = h;
		pattern = new Object[w*h];
	}
	
	public CRecipeShapedBase(Object... p) {
		String shape = "";
		int idx = 0;

		if (p[idx] instanceof Boolean) {
			allowMirror = (Boolean)p[idx];
			if (p[idx+1] instanceof Object[]) {
				p = (Object[])p[idx+1];
			} else {
				idx = 1;
			}
		}

		if (p[idx] instanceof String[]) {
			String[] parts = ((String[])p[idx++]);

			for (String s : parts) {
				width = s.length();
				shape += s;
			}

			height = parts.length;
		} else {
			while (p[idx] instanceof String) {
				String s = (String)p[idx++];
				shape += s;
				width = s.length();
				height++;
			}
		}

		if (width * height != shape.length()) {
			String ret = "Invalid shaped ore recipe: ";
			for (Object tmp :  p) {
				ret += tmp + ", ";
			}
			throw new RuntimeException(ret);
		}

		HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

		for (; idx < p.length; idx += 2) {
			Character chr = (Character)p[idx];
			Object in = p[idx + 1];

			if (in instanceof ItemStack) {
				itemMap.put(chr, ((ItemStack)in).copy());
			} else if (in instanceof Item) {
				itemMap.put(chr, new ItemStack((Item)in));
			} else if (in instanceof Block) {
				itemMap.put(chr, new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
			} else if (in instanceof String) {
				itemMap.put(chr, OreDictionary.getOres((String)in));
			} else if (in instanceof List<?>) {
				itemMap.put(chr, in);
			} else {
				String ret = "Invalid shaped ore recipe: ";
				for (Object tmp :  p) {
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
				if (this.checkMatch(inv, x, y, false)) return true;
				if (allowMirror && this.checkMatch(inv, x, y, true)) return true;
			}
		}

		return false;
	}
	
	protected boolean checkMatch(InventoryCrafting inv, int offx, int offy, boolean mirror) {
		for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
			for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
				int subX = x - offx;
				int subY = y - offy;
				Object target = null;

				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
					if (mirror) {
						target = pattern[width - subX - 1 + subY * width];
					} else {
						target = pattern[subX + subY * width];
					}
				}
				ItemStack slot = inv.getStackInRowAndColumn(x, y);

				if (target != null) {
					if (!StackUtils.checkItemEquals(target, slot)) return false;
				} else if (slot!=null) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int getRecipeSize() {
		return width*height;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Object[] getPattern() {
		return pattern;
	}

	/**
	 * Adding this annotation to subclasses will allow MLCore's NEI handler to pickup and process this recipe.<br/>
	 * This should only be used if you are overriding the output only. Do not use this when doing fancy stuff with input matching.
	 * @author Matchlighter
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface AutoNEI {}
}
