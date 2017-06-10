package ml.core.gui.controls;

import ml.core.gui.core.GuiElement;
import ml.core.gui.event.EventChanged;
import ml.core.gui.event.EventFocusLost;
import ml.core.gui.event.EventKeyPressed;
import ml.core.gui.event.GuiEvent;
import ml.core.gui.event.mouse.EventMouseDown;
import ml.core.vec.Vector2i;
import net.minecraft.client.gui.GuiTextField;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Note that this does NOT handle sending data to the server. It is up to you to decide when and how to do that.
 * @author Matchlighter
 */
public class ControlTextInput extends GuiControl {

	@SideOnly(Side.CLIENT)
	public GuiTextField textBox;
	
	public ControlTextInput(GuiElement parent, Vector2i pos, Vector2i size) {
		super(parent, pos, size);
		if (getSide() == Side.CLIENT) {
			textBox = new GuiTextField(getTopParent().getGui().getMinecraft().fontRenderer, 0, 0, size.x, size.y);
			textBox.setEnableBackgroundDrawing(false);
		}
	}
	
	@Override
	public void drawBackground(float partialTick) {
		//GL11.glTranslatef(getPosition().x, getPosition().y, 0);
		//TODO Render TB background
		textBox.drawTextBox();
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt.origin == this && evt instanceof EventMouseDown) {
			if (!hasFocus()) {
				takeFocus();
				textBox.setFocused(true);
			}
			EventMouseDown msev = (EventMouseDown)evt;
			textBox.mouseClicked(msev.mPos.x, msev.mPos.y, msev.button.ordinal());
			
		} else if (evt.origin == this && evt instanceof EventFocusLost && getSide() == Side.CLIENT) {
			textBox.setFocused(false);
			
		} else if (hasFocus() && evt instanceof EventKeyPressed) {
			evt.cancel();
			EventKeyPressed kpevt = (EventKeyPressed)evt;
			textBox.textboxKeyTyped(kpevt.character, kpevt.key);
			injectEvent(new EventChanged(this));
		}
		super.handleEvent(evt);
	}
	
	@SideOnly(Side.CLIENT)
	public GuiTextField getTextField() {
		return textBox;
	}
}
