package ml.core.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import ml.core.gui.GuiControl;

public class ControlButton extends GuiControl {

	private static class whGuiButton extends GuiButton {
		public whGuiButton() {
			super(0, 0, 0, "");
		}
		
		public void setWH(int w, int h) {
			this.width = w;
			this.height = h;
		}
	}
	
	private whGuiButton proxyBtn = new whGuiButton();
	
	public String text;
	
	public ControlButton(int xPos, int yPos, int w, int h, String t) {
		super(xPos, yPos, w, h);
		text = t;
	}

	@Override
	public void performRender(Minecraft mc, int mouseX, int mouseY) {
		proxyBtn.xPosition = X;
		proxyBtn.yPosition = Y;
		proxyBtn.setWH(width, height);
		proxyBtn.displayString = text;
		proxyBtn.enabled = enabled;
		
		proxyBtn.drawButton(mc, mouseX, mouseY);
	}

}
