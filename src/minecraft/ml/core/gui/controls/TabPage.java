package ml.core.gui.controls;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.gui.controls.ControlTabManager.GuiTab;

@SideOnly(Side.CLIENT)
public class TabPage extends GuiTab {

	public TabPage(ControlTabManager ctm) {
		super(ctm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void renderContents(Minecraft mc, int mX, int mY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWidth() {
		return defaultSize;
	}

	@Override
	public int getHeight() {
		return defaultSize;
	}

}
