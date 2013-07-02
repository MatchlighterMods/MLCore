package ml.core.gui;

import java.util.ArrayList;
import java.util.List;

import ml.core.geo.Rectangle;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerControl extends GuiContainer {

	protected List<GuiControl> controls = new ArrayList<GuiControl>();

	public GuiContainerControl(Container par1Container) {
		super(par1Container);

		initControls();
	}

	public int getWinWidth() {return xSize;}
	public int getWinHeight() {return ySize;}
	
	public Rectangle getWinBounds() {
		return new Rectangle(guiLeft, guiTop, xSize, ySize);
	}

	protected abstract void initControls();

	protected GuiControl getControlAt(int x, int y) {
		GuiControl ret = null;
		float cZind = 0;
		for (GuiControl ctrl : controls) {
			if (ctrl.isPointIn(x, y) && ctrl.zLevel>=cZind) {
				ret = ctrl;
				cZind = ret.zLevel;
			}
		}
		return ret;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		for (GuiControl gc : controls) {
			//if (gc.enabled)
			gc.update();
		}
	}

	protected abstract void drawBackgroundLayer(float f, int i, int j);

	@Override
	protected final void drawGuiContainerBackgroundLayer(float f, int mX, int mY) {
		mX -= guiLeft;
		mY -= guiTop;
		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0);
		for (GuiControl ctrl : controls) ctrl.renderFirst(mc, mX, mY);
		drawBackgroundLayer(f, mX, mY);
		for (GuiControl ctrl : controls) ctrl.renderBackground(mc, mX, mY);
		GL11.glPopMatrix();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mX, int mY) {
		GL11.glPushMatrix();
		GL11.glTranslatef(guiLeft, guiTop, 0);
		for (GuiControl ctrl : controls) ctrl.renderForeground(mc, mX-guiLeft, mY-guiTop);
		super.drawGuiContainerForegroundLayer(mX, mY);
		GL11.glPopMatrix();
	}

	@Override
	protected void mouseClicked(int mX, int mY, int btn) {
		GuiControl gc = getControlAt(mX-guiLeft, mY-guiTop);
		if (gc != null && gc.enabled && gc.onMouseClicked(mX-guiLeft, mY-guiTop, MouseButton.get(btn))) {
			return;
		}

		super.mouseClicked(mX, mY, btn);
	}

	@Override
	protected void mouseMovedOrUp(int mX, int mY, int act) {
		for (GuiControl gc : controls) {
			if (gc.enabled) {
				gc.onMouseMovedOrUp(mX-guiLeft, mY-guiTop, act);
			}
		}
		super.mouseMovedOrUp(mX, mY, act);
	}

	@Override
	protected void keyTyped(char chr, int key) {
		for (GuiControl gc : controls) {
			if (gc.enabled && gc.onKeyPress(chr, key))
				return;
		}
		super.keyTyped(chr, key);
	}

}
