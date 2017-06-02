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
	public void drawBackground(float partialTick) {
		bindStyleTexture("window");
		GuiRenderUtils.drawSlicedRect(this.getLocalPosition().x, this.getLocalPosition().y, this.getSize().x, this.getSize().y, 0, 0, 256, 256, 6);
		super.drawBackground(partialTick);
	}
	
	@Override
	public void handleEvent(GuiEvent evt) {
		// Don't need Closing KeyHandler; GuiContainer will do it if the event isn't cancelled
	}
	
	public void close() {
		this.player.closeScreen();
	}
	
}
