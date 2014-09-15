package ml.core.gui.core;

import java.util.ArrayList;
import java.util.List;

import ml.core.enums.MouseButton;
import ml.core.gui.controls.inventory.ControlSlot;
import ml.core.gui.core.GuiElement.RenderStage;
import ml.core.gui.event.EventKeyPressed;
import ml.core.gui.event.mouse.EventMouseClicked;
import ml.core.gui.event.mouse.EventMouseDown;
import ml.core.gui.event.mouse.EventMouseEntered;
import ml.core.gui.event.mouse.EventMouseLeave;
import ml.core.gui.event.mouse.EventMouseMove;
import ml.core.gui.event.mouse.EventMouseScroll;
import ml.core.gui.event.mouse.EventMouseUp;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// All this could be a lot cleaner, but that is the cost of trying to maintain universal compatibility with things such as NEI
@SideOnly(Side.CLIENT)
public class MLGuiClient extends GuiContainer {

	protected TopParentGuiElement priElemement;
	
	public MLGuiClient(TopParentGuiElement elm) {
		super(elm.getContainer());
		priElemement = elm;
	}
	
	public TopParentGuiElement getPrimaryElement() {
		return priElemement;
	}
	
	private List<Slot> eSlots = new ArrayList<Slot>();
	@Override
	public void drawScreen(int mX, int mY, float partialTick) {
		if (mX != priElemement.gmousePos.x || mY != priElemement.gmousePos.y) {
			priElemement.injectEvent(new EventMouseMove(priElemement, new Vector2i(mX, mY).minus(priElemement.gmousePos)));
			priElemement.gmousePos.set(mX, mY);
			
			GuiElement newHover = priElemement.findElementAtLocal(new Vector2i(mX, mY).minus(priElemement.getLocalPosition()));

			if (newHover != priElemement.hoverElement) {
				priElemement.injectEvent(new EventMouseLeave(priElemement.hoverElement));
				priElemement.hoverElement = newHover;
				priElemement.injectEvent(new EventMouseEntered(priElemement.hoverElement));
			}
		}
		super.drawScreen(mX, mY, partialTick);
		matrixAndDraw(RenderStage.Overlay, partialTick);
		
		if (priElemement.hoverElement != null) {
			priElemement.hoverElement.drawTooltip(mX, mY, partialTick);
		}
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
		GuiElement ge = priElemement.findElementAtLocal(new Vector2i(par1, par2).minus(getPosition()));
		if (ge instanceof ControlSlot)
			return ((ControlSlot)ge).getSlot();
		return null;
	}
	
	/**
	 * Calls {@link TopParentGuiElement#drawElement(RenderStage, float)} wrapped in a new GL Matrix<br/>
	 * Mostly just for DRY code
	 * @param stage
	 */
	private void matrixAndDraw(RenderStage stage, float partialTick) {
		//GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		priElemement.drawElement(stage, partialTick);
		//GL11.glPopMatrix();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		matrixAndDraw(RenderStage.Background, f);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		GL11.glEnable(GL11.GL_DEPTH_TEST); //Fixes held item rendering
	}
	
	@Override
	protected void drawSlotInventory(Slot par1Slot) {}
	
	public void drawSpecialSlotInventory(ControlSlot slt) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-slt.getSlot().xDisplayPosition+1, -slt.getSlot().yDisplayPosition+1, 0F);
		super.drawSlotInventory(slt.getSlot());
		GL11.glPopMatrix();
	}
	
	@Override
	public void drawHoveringText(List lines, int mX, int mY, FontRenderer font) {
		super.drawHoveringText(lines, mX, mY, font);
	}
	
	private GuiElement mouseDownEl;
	private long mouseDownTime;
	
	@Override
	protected void mouseClicked(int mX, int mY, int btn) {
		if (!priElemement.injectEvent(new EventMouseDown(priElemement.hoverElement, new Vector2i(mX, mY), MouseButton.get(btn))).cancelled) {
			if (priElemement.hoverElement != null) {
				mouseDownEl = priElemement.hoverElement;
				mouseDownTime = Minecraft.getSystemTime();
			}
			super.mouseClicked(mX, mY, btn);
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int mX, int mY, int which) {
		if (which > -1) {
			priElemement.injectEvent(new EventMouseUp(priElemement, new Vector2i(mX, mY), MouseButton.get(which)));
			if (mouseDownEl == priElemement.hoverElement) {
				priElemement.injectEvent(new EventMouseClicked(mouseDownEl, new Vector2i(mX, mY), MouseButton.get(which), (int)(Minecraft.getSystemTime()-mouseDownTime)));
			}
		} else {
			
		}
		super.mouseMovedOrUp(mX, mY, which);
	}

	@Override
	protected void mouseClickMove(int mX, int mY, int lastButtonClicked, long timeSinceMouseClick) {
		super.mouseClickMove(mX, mY, lastButtonClicked, timeSinceMouseClick);
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		
		int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		int dWheel = Mouse.getDWheel();
		if (dWheel != 0) {
			priElemement.injectEvent(new EventMouseScroll(priElemement.hoverElement, new Vector2i(i, j), dWheel));
		}
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
		return this.mc != null ? this.mc : FMLClientHandler.instance().getClient();
	}
}
