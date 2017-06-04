package ml.core.nei;

import java.util.List;

import ml.core.item.recipe.CRecipeShapedBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.InventoryCraftingDummy;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class MLShapedRecipeHandler extends ShapedRecipeHandler {
	
	private InventoryCrafting invCrafter = new InventoryCraftingDummy();
	
	public class CachedMLShapedRecipe extends CachedShapedRecipe {

		private final CRecipeShapedBase recipe;
		
		public CachedMLShapedRecipe(CRecipeShapedBase recipe) {
			super(recipe.getWidth(), recipe.getHeight(), recipe.getPattern(), recipe.getRecipeOutput());
			this.recipe = recipe;
		}
		
		@Override
		public List<PositionedStack> getIngredients() {
			List<PositionedStack> ingreds = super.getIngredients();
			for (int i=0; i<9; i++) {
				int x = i%3, y = i/3;
				invCrafter.setInventorySlotContents(x*3+y, (ingreds.size() > i && ingreds.get(i) != null) ? ingreds.get(i).item : null);
			}
			
			if (recipe.matches(invCrafter, null))
				this.result = new PositionedStack(recipe.getCraftingResult(invCrafter), 119, 24);
			return ingreds;
		}
		
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals("crafting") && getClass() == MLShapedRecipeHandler.class) {
			List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
			for(IRecipe irecipe : allrecipes) {
				if (irecipe instanceof CRecipeShapedBase && irecipe.getClass().getAnnotation(CRecipeShapedBase.AutoNEI.class) != null) {
					CRecipeShapedBase crsb = (CRecipeShapedBase)irecipe;
					CachedMLShapedRecipe recipe = new CachedMLShapedRecipe(crsb);
					recipe.computeVisuals();
					arecipes.add(recipe);
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe irecipe : allrecipes) {
			if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
				if (irecipe instanceof CRecipeShapedBase && irecipe.getClass().getAnnotation(CRecipeShapedBase.AutoNEI.class) != null) {
					CRecipeShapedBase crsb = (CRecipeShapedBase)irecipe;
					CachedMLShapedRecipe recipe = new CachedMLShapedRecipe(crsb);
					recipe.computeVisuals();
					arecipes.add(recipe);
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe irecipe : allrecipes) {
			if (irecipe instanceof CRecipeShapedBase && irecipe.getClass().getAnnotation(CRecipeShapedBase.AutoNEI.class) != null) {
				CRecipeShapedBase crsb = (CRecipeShapedBase)irecipe;
				CachedMLShapedRecipe recipe = new CachedMLShapedRecipe(crsb);
				
				if(recipe == null || !recipe.contains(recipe.ingredients, ingredient))
					continue;

				recipe.computeVisuals();
				if(recipe.contains(recipe.ingredients, ingredient)) {
					recipe.setIngredientPermutation(recipe.ingredients, ingredient);
					arecipes.add(recipe);
				}
			}
		}
	}
}
