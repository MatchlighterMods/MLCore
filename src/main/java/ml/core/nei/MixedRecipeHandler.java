package ml.core.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ml.core.item.recipe.RecipeMixed;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.InventoryCraftingDummy;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class MixedRecipeHandler extends ShapedRecipeHandler {

	public class CachedMixedRecipe extends CachedShapedRecipe {

		private final RecipeMixed recipe;
		private InventoryCrafting invCrafting = new InventoryCraftingDummy();

		public CachedMixedRecipe(RecipeMixed recipe) {
			super(recipe.getWidth(), recipe.getHeight(), recipe.getPattern(), recipe.getRecipeOutput());
			this.recipe = recipe;
		}

		@Override
		public List<PositionedStack> getIngredients() {
			List<PositionedStack> ingreds = new ArrayList<PositionedStack>();

			List<Object[]> shapelessSets = new ArrayList<Object[]>();
			recipe.addShapelessItemsToNEI(shapelessSets);

			int currentSet = (cycleticks / 20) % (shapelessSets.size() + 1);
			Object[] itemSet = currentSet > 0 ? shapelessSets.get(currentSet-1) : new Object[]{};

			LinkedList<Object> setQueue = new LinkedList<Object>();
			setQueue.addAll(Arrays.asList(itemSet));

			Object[] pattern = recipe.getPattern();

			for (int x=0; x < recipe.MAX_CRAFT_GRID_WIDTH; x++) {
				for (int y=0; y < recipe.MAX_CRAFT_GRID_WIDTH; y++) {

					Object nitem = null;
					if (x < recipe.getWidth() && y < recipe.getHeight())
						nitem = pattern[x + y*recipe.getWidth()];

					if (nitem == null) {
						nitem = setQueue.poll();
					}

					invCrafting.setInventorySlotContents(x+y*3, null);
					if (nitem != null) {
						PositionedStack stack = new PositionedStack(nitem, 25+x*18, 6+y*18, false);
						stack.setMaxSize(1);
						randomRenderPermutation(stack, cycleticks/20);
						ingreds.add(stack);
						invCrafting.setInventorySlotContents(x+y*3, stack.item);
					}

				}
			}

			return ingreds;
		}

		@Override
		public PositionedStack getResult() {
			return new PositionedStack(recipe.getCraftingResult(invCrafting), 119, 24);
		}
		
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId == "crafting"){
			List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
			for(IRecipe irecipe : allrecipes) {
				if (irecipe instanceof RecipeMixed) {
					CachedMixedRecipe recipe = new CachedMixedRecipe((RecipeMixed)irecipe);

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
				if (irecipe instanceof RecipeMixed) {
					CachedMixedRecipe recipe = new CachedMixedRecipe((RecipeMixed)irecipe);

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
			if (irecipe instanceof RecipeMixed) {
				CachedMixedRecipe recipe = new CachedMixedRecipe((RecipeMixed)irecipe);

				if (!recipe.contains(recipe.ingredients, ingredient))
					continue;

				recipe.computeVisuals();
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}
}
