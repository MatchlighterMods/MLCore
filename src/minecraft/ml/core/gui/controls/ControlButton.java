package ml.core.gui.controls;

import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiPositionedControl;
import ml.core.gui.GuiRenderUtils;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ControlButton extends GuiPositionedControl {
	
	public String text;
	
	public ControlButton(GuiContainerControl assocGui, int xPos, int yPos, int w, int h, String t) {
		super(assocGui, xPos, yPos, w, h);
		text = t;
	}

	@Override
	public void renderForeground(Minecraft mc, int mouseX, int mouseY) {
		mc.renderEngine.bindTexture("/gui/gui.png");
		GuiRenderUtils.drawSlicedRect(bounds.xCoord, bounds.yCoord, bounds.width, bounds.height, 0, enabled ? isPointIn(mouseX, mouseY) ? 86 : 66 : 46, 200, 20, 2, 2, 2, 2);
	}

}
