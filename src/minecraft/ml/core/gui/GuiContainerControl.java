package ml.core.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiContainerControl extends GuiContainer {
	
	protected List<GuiControl> controls = new ArrayList<GuiControl>();

	public GuiContainerControl(Container par1Container) {
		super(par1Container);
		// TODO Auto-generated constructor stub
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
		GuiControl gc = getControlAt(mX, mY);
		if (gc != null && gc.enabled && gc.onMouseClicked(mX, mY, MouseButton.get(btn))) {
			return;
		}
		
		super.mouseClicked(mX, mY, btn);
	}
	
	@Override
	protected void mouseMovedOrUp(int mX, int mY, int act) {
		for (GuiControl gc : controls) {
			if (gc.enabled) {
				gc.onMouseMovedOrUp(mX, mY, act);
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
