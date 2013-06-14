package ml.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.geo.GeoMath.XYPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

@SideOnly(Side.CLIENT)
public abstract class GuiComponent extends Gui {
	
	protected GuiContainerControl guiContainer;
	
	public GuiComponent(GuiContainerControl gcc) {
		guiContainer = gcc;
	}

	public abstract boolean pointIn(int pX, int pY);
	
	public abstract void performRender(Minecraft mc, int mouseX, int mouseY);
	
	public void mouseClicked(int mouseX, int mouseY, MouseButton button) {}
	
	public void onHoverKeyPress(int key) {}
	
	public void bindTexture(String t) {
		guiContainer.
	}
}
