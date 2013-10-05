package ml.core.gui.core;

import ml.core.gui.event.GuiEvent;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;


public abstract class Window extends TopParentGuiElement {

	public Window(EntityPlayer epl, Side side) {
		super(epl, side);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawBackground() {
		// TODO Draw window frame
		super.drawBackground();
	}
	
	@Override
	public void handleEvent(GuiEvent evt) {
		super.handleEvent(evt);
		
//		if (evt instanceof EventKeyPressed && !evt.cancelled) { //  Not necessary (see GuiContainer)
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
