package ml.core.item.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class ShapedRecipe extends CRecipeShapedBase {

	public final ItemStack result;
	
	public ShapedRecipe(ItemStack result, Object... p) {
		super(p);
		this.result = result;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
		return result.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

}
