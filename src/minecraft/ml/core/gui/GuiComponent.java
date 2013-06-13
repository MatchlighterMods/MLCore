package ml.core.gui;

import ml.core.geo.GeoMath.XYPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class GuiComponent extends Gui {

	public abstract boolean pointIn(int pX, int pY);
	
	public abstract void performRender(Minecraft mc, int mouseX, int mouseY);
	
	public void mouseClicked(int mouseX, int mouseY, MouseButton button) {}
	
	public void onHoverKeyPress(int key) {}
}
