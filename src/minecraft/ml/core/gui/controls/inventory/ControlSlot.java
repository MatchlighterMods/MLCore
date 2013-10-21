package ml.core.gui.controls.inventory;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.gui.event.EventMouseClicked;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;
import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

public class ControlSlot extends GuiControl {

	protected Slot slot;
	public int hoverColor = -2130706433;

	public ControlSlot(GuiElement par, Slot slt, Vector2i pos, Vector2i size) {
		super(par, pos, size);
		slot = slt;
		getTopParent().getContainer().addSlotToContainer(slt);
	}

	@Override
	public void drawBackground() {
		GL11.glTranslatef(getPosition().x, getPosition().y, 0);
		GL11.glScalef(((float)getSize().x-2F)/16F, ((float)getSize().y-2F)/16F, 1F);
		// TODO Draw background

		if (hasHover()) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GuiRenderUtils.drawGradientRect(1, 1, 1 + 16, 1 + 16, hoverColor, hoverColor);
			GL11.glEnable(GL11.GL_LIGHTING);
		}

		super.drawBackground();
	}
	
	@Override
	public void guiTick() {
		Vector2i abPos = getAbsolutePosition().minus(getTopParent().getPosition());
		slot.xDisplayPosition = abPos.x+1;
		slot.yDisplayPosition = abPos.y+1;
		super.guiTick();
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (slot != null && evt instanceof EventMouseClicked && evt.origin == this) {
			EventMouseClicked evmc = (EventMouseClicked)evt;
		}
		super.handleEvent(evt);
	}

	public Slot getSlot() {
		return slot;
	}

}
