package ml.core.gui.controls.inventory;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.MLSlot;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.gui.core.SlotCycler.IStackMergeTarget;
import ml.core.gui.event.GuiEvent;
import ml.core.gui.event.mouse.EventMouseDown;
import ml.core.vec.Vector2i;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ControlSlot extends GuiControl implements IStackMergeTarget {

	protected Slot slot;
	
	public boolean renderBackground = true;
	public boolean renderHover = true;

	public ControlSlot(GuiElement par, Slot slt, Vector2i pos, Vector2i size) {
		super(par, pos, size);
		slot = slt;
		if (slot instanceof MLSlot)
			((MLSlot)slot).controlSlot = this;
		getTopParent().getContainer().addSlotToContainer(slt);
	}
	
	public ControlSlot(GuiElement par, Slot slt, Vector2i size) {
		this(par, slt, new Vector2i(slt.xDisplayPosition, slt.yDisplayPosition), size);
	}

	public ControlSlot(GuiElement par, Slot slt) {
		this(par, slt, new Vector2i(slt.xDisplayPosition, slt.yDisplayPosition), new Vector2i(18, 18));
	}
	
	@Override
	public void drawBackground(float partialTick) {
		GL11.glScalef((float)getSize().x/18F, (float)getSize().y/18F, 1F);

		if (renderBackground) {
			bindStyleTexture("slot");
			GuiRenderUtils.drawTexturedModelRect(0, 0, 0, 0, 1, 1, 18, 18);
		}

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)240 / 1.0F, (float)240 / 1.0F);
		getGui().drawSpecialSlotInventory(this);
		GL11.glDisable(GL11.GL_LIGHTING);

		if (hasHover() && !slot.func_111238_b() && renderHover) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int hoverColor = getStyle().getColorValue("slot.hover");
			GuiRenderUtils.drawGradientRect(1, 1, 1 + 16, 1 + 16, hoverColor, hoverColor);
		}

		super.drawBackground(partialTick);
	}

	@Override
	public void drawTooltip(int mX, int mY, float partialTick) { // Leaving this native so we don't conflict with NEI
		//getGui().drawSpecialItemStackTooltip(slot.getStack(), mX, mY);
	}
	
	@Override
	public void guiTick() {
		Vector2i abPos = getGlobalPosition().minus(getTopParent().getLocalPosition());
		slot.xDisplayPosition = abPos.x+1;
		slot.yDisplayPosition = abPos.y+1;
		super.guiTick();
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (slot != null && evt instanceof EventMouseDown && evt.origin == this) {
			EventMouseDown evmc = (EventMouseDown)evt;
		}
		super.handleEvent(evt);
	}

	public Slot getSlot() {
		return slot;
	}

	@Override
	public boolean mergeStackInto(ItemStack is) {
		// TODO Auto-generated method stub
		return false;
	}

}
