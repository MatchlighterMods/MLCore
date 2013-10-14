package ml.core.gui.core;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.event.GuiEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;

public abstract class Window extends TopParentGuiElement {
	
	protected ResourceLocation bg = new ResourceLocation("MLCore:textures/gui/window.png");

	public Window(EntityPlayer epl, Side side) {
		super(epl, side);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawBackground() {
		bindTexture(bg);
		GuiRenderUtils.drawSlicedRect(this.getLocalPosition().x, this.getLocalPosition().y, this.getSize().x, this.getSize().y, 0, 0, 256, 256, 4, 4, 4, 4);
		super.drawBackground();
	}
	
	@Override
	public void handleEvent(GuiEvent evt) {
		super.handleEvent(evt);
		
//		if (evt instanceof EventKeyPressed && !evt.cancelled) { // Not necessary (see GuiContainer)
//			EventKeyPressed evkp = (EventKeyPressed)evt;
//			if (evkp.character == getGui().getMinecraft().gameSettings.keyBindInventory.keyCode) {
//				// TODO Close Gui
//			}
//		}
	}
	
	public static enum WindowSide {
		Top,
		Right,
		Bottom,
		Left;
	}
}
