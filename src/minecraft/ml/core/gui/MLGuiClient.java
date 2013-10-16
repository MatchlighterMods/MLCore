package ml.core.gui;

import org.lwjgl.opengl.GL11;

import ml.core.enums.MouseButton;
import ml.core.gui.core.GuiElement;
import ml.core.gui.core.TopParentGuiElement;
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

public class MLGuiClient extends GuiContainer {

	protected TopParentGuiElement priElemement;
	
	public MLGuiClient(TopParentGuiElement elm) {
		super(elm.getContainer());
		priElemement = elm;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
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
	
	/**
	 * Calls {@link TopParentGuiElement#drawElement(RenderStage)} wrapped in a new GL Matrix<br/>
	 * Mostly just for DRY code
	 * @param stage
	 */
	private void matrixAndDraw(RenderStage stage) {
		GL11.glPushMatrix();
		priElemement.drawElement(stage);
		GL11.glPopMatrix();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		matrixAndDraw(RenderStage.Background);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		matrixAndDraw(RenderStage.Foreground);
		super.drawGuiContainerForegroundLayer(par1, par2);
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
			if (mX != priElemement.gmousePos.x || mY != priElemement.gmousePos.y) {
				priElemement.injectEvent(new EventMouseMove(priElemement, new Vector2i(mX, mY).minus(priElemement.gmousePos)));
				priElemement.gmousePos.set(mX, mY);
				
				GuiElement newHover = priElemement.findElementAt(new Vector2i(mX, mY));
				if (newHover != priElemement.hoverElement) {
					priElemement.injectEvent(new EventMouseLeave(priElemement.hoverElement));
					priElemement.hoverElement = newHover;
					priElemement.injectEvent(new EventMouseEntered(priElemement.hoverElement));
				}
			}
		}
		super.mouseMovedOrUp(mX, mY, which);
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
