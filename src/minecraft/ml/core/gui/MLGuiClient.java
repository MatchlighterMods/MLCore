package ml.core.gui;

import ml.core.enums.MouseButton;
import ml.core.gui.core.TopParentGuiElement;
import ml.core.gui.event.EventKeyPressed;
import ml.core.gui.event.EventMouseClicked;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

public class MLGuiClient extends GuiContainer {

	protected TopParentGuiElement priElemement;
	
	public MLGuiClient(TopParentGuiElement elm) {
		super(elm.getContainer());
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		priElemement.gmousePos.set(par1, par2);
		if (par1 != priElemement.gmousePos.x || par2 != priElemement.gmousePos.y) { // TODO Enter/leave events
			priElemement.hoverElement = priElemement.findElementAt(new Vector2i(par1, par2));
		}
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		this.width = priElemement.size.x;
		this.height = priElemement.size.y;
		
		this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
		priElemement.guiTick();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		priElemement.drawBackground();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		priElemement.drawForeground();
		super.drawGuiContainerForegroundLayer(par1, par2);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (!priElemement.injectEvent(new EventMouseClicked(priElemement.hoverElement, par1, par2, MouseButton.get(par3))).cancelled)
			super.mouseClicked(par1, par2, par3);
	}
	
	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		if (par3 > -1) {
			
		}
		super.mouseMovedOrUp(par1, par2, par3);
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
