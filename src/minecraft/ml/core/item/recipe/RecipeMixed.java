package ml.core.item.recipe;

import java.util.ArrayList;
import java.util.List;

import ml.core.item.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * Thanks to copyboy for the idea on how to implement this.
 * @author Matchlighter
 */
public abstract class RecipeMixed extends CRecipeShapedBase implements IRecipe {

	protected ItemStack output = null;

	public RecipeMixed(Block     result, Object[] recipe){ this(new ItemStack(result), recipe); }
	public RecipeMixed(Item      result, Object[] recipe){ this(new ItemStack(result), recipe); }
	public RecipeMixed(ItemStack result, Object[] recipe) {
		super(recipe);
		output = result.copy();
	}

	public abstract boolean shapelessItemsValid(InventoryCrafting inv, List<ItemStack> items);

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		return output.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		List<ItemStack> shapelessStacks = new ArrayList<ItemStack>();

		for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
			for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
				int subX = x - startX;
				int subY = y - startY;
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
					if (!ItemUtils.checkItemEquals(target, slot)) return false;
				} else if (slot!=null) {
					shapelessStacks.add(slot);
				}
			}
		}
		
		if (shapelessStacks.size() > 0 && !shapelessItemsValid(inv, shapelessStacks))
			return false;

		return true;
	}
}
