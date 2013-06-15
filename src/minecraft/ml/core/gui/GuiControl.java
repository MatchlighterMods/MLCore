package ml.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.geo.GeoMath.XYPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

@SideOnly(Side.CLIENT)
public abstract class GuiControl extends Gui {
	
	protected GuiContainerControl guiContainer;
	
	public GuiControl(GuiContainerControl gcc) {
		guiContainer = gcc;
	}

	/**
	 * Must return true to receive events
	 */
	public abstract boolean isPointIn(int pX, int pY);
	
	public abstract void performRender(Minecraft mc, int mouseX, int mouseY);
	
	public boolean onMouseClicked(int mouseX, int mouseY, MouseButton button) {
		return false;
	}
	
	public void onMouseMovedOrUp() {}
	
	public void onHoverKeyPress(int key) {}
}
