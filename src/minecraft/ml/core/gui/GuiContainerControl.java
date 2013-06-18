package ml.core.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerControl extends GuiContainer {
	
	protected List<GuiControl> controls = new ArrayList<GuiControl>();

	public GuiContainerControl(Container par1Container) {
		super(par1Container);
		// TODO Auto-generated constructor stub
	}

	public int getWinWidth() {return xSize;}
	public int getWinHeight() {return ySize;}
	
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
			gc.update();
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		// TODO Auto-generated method stub
		super.drawGuiContainerForegroundLayer(par1, par2);
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
