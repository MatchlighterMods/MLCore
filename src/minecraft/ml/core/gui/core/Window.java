package ml.core.gui.core;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.event.GuiEvent;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;

public abstract class Window extends TopParentGuiElement {
	
	public Window(EntityPlayer epl, Side side) {
		super(epl, side);
	}

	@Override
	public void drawBackground() {
		bindStyleTexture("window");
		GuiRenderUtils.drawSlicedRect(this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y, 0, 0, 256, 256, 6);
		super.drawBackground();
	}
	
	@Override
	public void handleEvent(GuiEvent evt) {
		// Don't need Closing KeyHandler; GuiContainer will do it if the event isn't cancelled
	}
	
	public static enum WindowSide {
		Top,
		Right,
		Bottom,
		Left;
	}
}
