package ml.core.gui.controls.tabs;

import ml.core.enums.MouseButton;
import ml.core.gui.controls.tabs.ControlTabManager.GuiTab;
import ml.core.gui.event.EventMouseClicked;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class TabLedger extends GuiTab {

	protected boolean openState = false;
	public Vector2i openSize;
	public Vector2i closeSize;
	
	public int sizingSpeed = 8;
	
	public TabLedger(ControlTabManager ctm, Vector2i oSize) {
		super(ctm);
		
		closeSize = new Vector2i(defaultSize, defaultSize);
		size = closeSize.copy();
		openSize = oSize.copy();
	}

	@Override
	public Vector2i getTargetSize() {
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
	public void handleEvent(GuiEvent evt) {
		if (evt.origin==this && evt instanceof EventMouseClicked && ((EventMouseClicked)evt).button==MouseButton.Left) {
			if (openState) closeLedger(); else openLedger();
		}
		super.handleEvent(evt);
	}

}
