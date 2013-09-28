package ml.core.item.recipe;

import java.util.HashMap;

import ml.core.item.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A simple ShapedRecipe clone that will call out to subclasses to match each slot
 * @author Matchlighter
 */
public abstract class RecipeShapedVariable extends CRecipeShapedBase implements IRecipe {
    
    public RecipeShapedVariable(Object... recipe) {
		super(recipe);
    }

	protected boolean checkMatch(InventoryCrafting inv, int offx, int offy, boolean mirror) {
		for (int x = 0; x < 3; ++x) {
			for (int y = 0; y < 3; ++y) {
				int lx = x - offx;
				int ly = y - offy;

				ItemStack itemstack1 = inv.getStackInRowAndColumn(x, y);
				
				if (lx>=0 && lx<this.width && ly>=0 && ly<this.height) {
					if (itemstack1 != null)
						return false;
				} else if (!itemMatchesAt(mirror ? this.width-lx-1 : lx, ly, itemstack1))
					return false;
			}
		}

		return true;
	}
	
	/**
	 * @param lx Localized x-position. Falls within 0&le;x&lt;width
	 * @param ly Localized y-position. Falls within 0&le;y&lt;hight
	 * @param is {@link ItemStack} in the slot
	 * @return
	 */
	public boolean itemMatchesAt(int lx, int ly, ItemStack is) {
		Object target = pattern[lx+ly*width];
		if (is != null || target != null) {
			if ((target == null && is != null) || (target != null && is == null))
				return false;
			
			if (!ItemUtils.checkItemEquals(target, is))
				return false;
		}
		return true;
	}

}
