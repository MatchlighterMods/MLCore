package ml.core.gui.controls;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.gui.controls.ControlTabManager.GuiTab;

@SideOnly(Side.CLIENT)
public class TabLedger extends GuiTab {

	public TabLedger(ControlTabManager ctm) {
		super(ctm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateTick() {
		// TODO Sizing logic
		super.updateTick();
	}
	
	public void openLedger() {
		for (GuiTab tab : TabManager.tabs) {
			if (tab instanceof TabLedger) {
				((TabLedger)tab).closeLedger();
			}
		}
		// TODO Open
	}
	
	public void closeLedger() {
		
	}
	
	@Override
	public void renderContents(Minecraft mc, int mX, int mY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
