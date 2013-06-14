package ml.core.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiControl;

public class ControlButton extends GuiControl {
	
	public String text;
	
	public ControlButton(GuiContainerControl assocGui, int xPos, int yPos, int w, int h, String t) {
		super(assocGui, xPos, yPos, w, h);
		text = t;
	}

	@Override
	public void performRender(Minecraft mc, int mouseX, int mouseY) {
		
	}

}
