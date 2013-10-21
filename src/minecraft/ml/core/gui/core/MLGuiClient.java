package ml.core.gui.core;

import java.util.ArrayList;
import java.util.List;

import ml.core.enums.MouseButton;
import ml.core.gui.controls.inventory.ControlSlot;
import ml.core.gui.core.GuiElement.RenderStage;
import ml.core.gui.event.EventKeyPressed;
import ml.core.gui.event.EventMouseClicked;
import ml.core.gui.event.EventMouseEntered;
import ml.core.gui.event.EventMouseLeave;
import ml.core.gui.event.EventMouseMove;
import ml.core.gui.event.EventMouseUp;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

// All this could be a lot cleaner, but that is the cost of trying to maintain universal compatibility with things such as NEI
public class MLGuiClient extends GuiContainer {

	protected TopParentGuiElement priElemement;
	
	public MLGuiClient(TopParentGuiElement elm) {
		super(elm.getContainer());
		priElemement = elm;
	}
	
	private List<Slot> eSlots = new ArrayList<Slot>();
	@Override
	public void drawScreen(int mX, int mY, float par3) {
		if (mX != priElemement.gmousePos.x || mY != priElemement.gmousePos.y) {
			priElemement.injectEvent(new EventMouseMove(priElemement, new Vector2i(mX, mY).minus(priElemement.gmousePos)));
			priElemement.gmousePos.set(mX, mY);
			
			GuiElement newHover = priElemement.findElementAtLocal(new Vector2i(mX, mY).minus(priElemement.getPosition()));

			if (newHover != priElemement.hoverElement) {
				priElemement.injectEvent(new EventMouseLeave(priElemement.hoverElement));
				priElemement.hoverElement = newHover;
				priElemement.injectEvent(new EventMouseEntered(priElemement.hoverElement));
			}
		}
		super.drawScreen(mX, mY, par3);
		matrixAndDraw(RenderStage.Overlay);
	}
	
	public void refreshSize() {
		this.xSize = priElemement.getSize().x;
		this.ySize = priElemement.getSize().y;
		
		this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		priElemement.guiTick();
	}
	
	@Override
	public Slot getSlotAtPosition(int par1, int par2) {
		if (priElemement.hoverElement instanceof ControlSlot)
			return ((ControlSlot)priElemement.hoverElement).getSlot();
		return null;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		//if (priElemement.slotManager == null)
			//priElemement.slotManager = new SlotManager(priElemement);
	}
	
	/**
	 * Calls {@link TopParentGuiElement#drawElement(RenderStage)} wrapped in a new GL Matrix<br/>
	 * Mostly just for DRY code
	 * @param stage
	 */
	private void matrixAndDraw(RenderStage stage) {
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		priElemement.drawElement(stage);
		GL11.glPopMatrix();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		matrixAndDraw(RenderStage.Background);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		GL11.glEnable(GL11.GL_DEPTH_TEST); //Fixes held item rendering
	}
	
	@Override
	protected void drawSlotInventory(Slot par1Slot) {
		GL11.glPushMatrix();
		for (GuiElement c : priElemement.getDescendants()) {
			if (c instanceof ControlSlot && ((ControlSlot)c).getSlot()==par1Slot) {
				Vector2i absPos = c.getAbsolutePosition().minus(getPosition());
				float xfact = ((float)c.getSize().x-2F)/16F;
				float yfact = ((float)c.getSize().y-2F)/16F;
				GL11.glTranslatef(absPos.x, absPos.y, 0F); // Which is faster?
				//GL11.glTranslatef(-absPos.x*(xfact-1F), -absPos.y*(yfact-1F), 0F);
				GL11.glScalef(xfact, yfact, 1F);
				GL11.glTranslatef(-absPos.x, -absPos.y, 0F);
			}
		}
		super.drawSlotInventory(par1Slot);
		GL11.glPopMatrix();
	}

	@Override
	protected void mouseClicked(int mX, int mY, int btn) {
		if (!priElemement.injectEvent(new EventMouseClicked(priElemement.hoverElement, new Vector2i(mX, mY), MouseButton.get(btn))).cancelled)
			super.mouseClicked(mX, mY, btn);
	}
	
	@Override
	protected void mouseMovedOrUp(int mX, int mY, int which) {
		if (which > -1) {
			priElemement.injectEvent(new EventMouseUp(priElemement, new Vector2i(mX, mY), MouseButton.get(which)));
		} else {
			
		}
		super.mouseMovedOrUp(mX, mY, which);
	}

	@Override
	protected void mouseClickMove(int mX, int mY, int lastButtonClicked, long timeSinceMouseClick) {
		super.mouseClickMove(mX, mY, lastButtonClicked, timeSinceMouseClick);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		if (!priElemement.injectEvent(new EventKeyPressed(priElemement, par1, par2)).cancelled)
			super.keyTyped(par1, par2);
	}
	
	public Vector2i getPosition() {
		return new Vector2i(guiLeft, guiTop);
	}
	
	public Minecraft getMinecraft() {
		return this.mc;
	}
}
