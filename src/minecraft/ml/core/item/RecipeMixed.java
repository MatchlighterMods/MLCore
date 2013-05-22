package ml.core.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Thanks to copyboy for the idea on how to implement this.
 * @author Matchlighter
 *
 */
public abstract class RecipeMixed implements IRecipe {

	protected static final int MAX_CRAFT_GRID_WIDTH = 3;
	protected static final int MAX_CRAFT_GRID_HEIGHT = 3;

	protected ItemStack output = null;
	protected Object[] input = null;
	protected int width = 0;
	protected int height = 0;
	protected boolean mirrored = true;

	public RecipeMixed(Block     result, Object... recipe){ this(new ItemStack(result), recipe); }
	public RecipeMixed(Item      result, Object... recipe){ this(new ItemStack(result), recipe); }
	public RecipeMixed(ItemStack result, Object... recipe) {
		output = result.copy();

		String shape = "";
		int idx = 0;

		if (recipe[idx] instanceof Boolean) {
			mirrored = (Boolean)recipe[idx];
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
			ret += output;
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
				itemMap.put(chr, in); //OreDictionary.getOres((String)in)
			} else {
				String ret = "Invalid shaped ore recipe: ";
				for (Object tmp :  recipe) {
					ret += tmp + ", ";
				}
				ret += output;
				throw new RuntimeException(ret);
			}
		}

		input = new Object[width * height];
		int x = 0;
		for (char chr : shape.toCharArray()) {
			input[x++] = itemMap.get(chr);
		}
	}

	public abstract boolean shapelessItemsValid(InventoryCrafting inv, List<ItemStack> items);

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		return output.copy();
	}

	@Override
	public int getRecipeSize() {
		return input.length;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++) {
			for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y) {

				if (checkMatch(inv, x, y, false)) return true;
				if (mirrored && checkMatch(inv, x, y, true)) return true;
			}
		}

		return false;
	}

	private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		List<ItemStack> shapelessStacks = new ArrayList<ItemStack>();

		for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
			for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
				int subX = x - startX;
				int subY = y - startY;
				Object target = null;

				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
					if (mirror) {
						target = input[width - subX - 1 + subY * width];
					} else {
						target = input[subX + subY * width];
					}
				}

				ItemStack slot = inv.getStackInRowAndColumn(x, y);

				if (target != null) {
					if (!checkItemEquals(target, slot)) return false;
//					if (target instanceof ItemStack) {
//						if (!checkItemEquals((ItemStack)target, slot)) return false;
//					} else if (target instanceof ArrayList) {
//						boolean matched = false;
//
//						for (ItemStack item : (ArrayList<ItemStack>)target) {
//							matched = matched || checkItemEquals(item, slot);
//						}
//
//						if (!matched) {
//							return false;
//						}
//					}
				} else if (slot!=null) {
					shapelessStacks.add(slot);
				} else {
					return false;
				}
			}
		}

		if (shapelessStacks.size() > 0 && !shapelessItemsValid(inv, shapelessStacks))
			return false;

		return true;
	}

	protected boolean checkItemEquals(Object target, ItemStack input) {
		if (input == null && target != null || input != null && target == null) {
			return false;
		}
		
		if (target instanceof String) {
			return OreDictionary.getOreID(input) == OreDictionary.getOreID((String)target);
		} else if (target instanceof ItemStack) {
			ItemStack trgIS = (ItemStack)target;
			return (trgIS.itemID == input.itemID && (trgIS.getItemDamage() == OreDictionary.WILDCARD_VALUE|| trgIS.getItemDamage() == input.getItemDamage()));
		}
		return false;
	}
}
