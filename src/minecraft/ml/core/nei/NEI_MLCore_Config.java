package ml.core.nei;

import ml.core.gui.core.GuiElement;
import ml.core.gui.core.MLGuiClient;
import ml.core.gui.core.TopParentGuiElement;
import ml.core.vec.Rectangle;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.VisiblityData;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.INEIGuiAdapter;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEI_MLCore_Config implements IConfigureNEI {

	private void registerCraftingUsage(TemplateRecipeHandler handler) {
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
	}
	
	@Override
	public void loadConfig() {
		API.registerNEIGuiHandler(new GuiHandler());
		
		registerCraftingUsage(new MixedRecipeHandler());
		registerCraftingUsage(new MLShapedRecipeHandler());
	}

	@Override
	public String getName() {
		return "MLCore";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	private class GuiHandler extends INEIGuiAdapter {
		@Override
		public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
			if (gui instanceof MLGuiClient) {
				MLGuiClient mlg = (MLGuiClient)gui;
				GuiElement elm = mlg.getPrimaryElement();
				for (GuiElement ge : elm.getDescendants()) {
					if (ge.getParent().isChildVisible(ge) && ge.getGlobalBounds().intersects(new Rectangle(x, y, w, h))) {
						return true;
					}
				}
			}
			return false;
		}
		
		@Override
		public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
			if (gui instanceof MLGuiClient) {
				MLGuiClient mlg = (MLGuiClient)gui;
				TopParentGuiElement elm = mlg.getPrimaryElement();
				currentVisibility.showNEI = elm.shouldShowNEI();
			}
			return super.modifyVisiblity(gui, currentVisibility);
		}
	}
}
