package ml.core.gui.controls;

import ml.core.gui.MouseButton;
import ml.core.gui.controls.ControlTabManager.GuiTab;
import ml.core.vec.Vector2;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class TabLedger extends GuiTab {

	protected boolean openState = false;
	public Vector2<Integer> openSize;
	public Vector2<Integer> closeSize;
	
	public int sizingSpeed = 8;
	
	public TabLedger(ControlTabManager ctm, Vector2<Integer> oSize) {
		super(ctm);
		
		closeSize = new Vector2<Integer>(defaultSize, defaultSize);
		size = closeSize.copy();
		openSize = oSize.copy();
	}

	@Override
	public Vector2<Integer> getTargetSize() {
		return openState ? openSize : closeSize;
	}
	
	public void openLedger() {
		for (GuiTab tab : TabManager.tabs) {
			if (tab instanceof TabLedger) {
				((TabLedger)tab).closeLedger();
			}
		}
		openState = true;
	}
	
	public void closeLedger() {
		openState = false;
	}
	
	@Override
	public abstract void renderContents(Minecraft mc, int mX, int mY);
	
	@Override
	public boolean onMouseClicked(int lmX, int lmY, MouseButton button) {
		if (openState) closeLedger(); else openLedger();
		return true;
	}

}
