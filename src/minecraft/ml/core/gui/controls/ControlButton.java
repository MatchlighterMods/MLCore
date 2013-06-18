package ml.core.gui.controls;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiPositionedControl;
import ml.core.gui.GuiRenderUtils;

@SideOnly(Side.CLIENT)
public class ControlButton extends GuiPositionedControl {
	
	public String text;
	
	public ControlButton(GuiContainerControl assocGui, int xPos, int yPos, int w, int h, String t) {
		super(assocGui, xPos, yPos, w, h);
		text = t;
	}

	@Override
	public void performRender(Minecraft mc, int mouseX, int mouseY) {
		mc.renderEngine.bindTexture("/gui/gui.png");
		GuiRenderUtils.drawSlicedRect(bounds.xCoord, bounds.yCoord, bounds.width, bounds.height, 0, enabled ? isPointIn(mouseX, mouseY) ? 86 : 66 : 46, 200, 20, 2, 2, 2, 2);
	}

}
