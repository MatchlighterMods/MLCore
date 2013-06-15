package ml.core.gui.controls;

import java.util.ArrayList;
import java.util.List;

import scala.annotation.meta.companionObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiControl;
import ml.core.gui.GuiPositionedControl;
import ml.core.gui.GuiSide;

public class ControlTabManager extends GuiControl {

	public static class GuiTab extends GuiPositionedControl {
		
		public ControlTabManager TabManager;
		
		public GuiTab(ControlTabManager ctm, int xPos, int yPos, int w,
				int h) {
			super(ctm.guiContainer, xPos, yPos, w, h);
			
			TabManager = ctm;
		}

		@Override
		public void performRender(Minecraft mc, int mouseX, int mouseY) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public GuiSide side;
	public List<GuiTab> tabs = new ArrayList<ControlTabManager.GuiTab>();

	public ControlTabManager(GuiContainerControl gcc, GuiSide sd) {
		super(gcc);
		
		side = sd;
	}

	@Override
	public boolean isPointIn(int pX, int pY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performRender(Minecraft mc, int mouseX, int mouseY) {
		// TODO Auto-generated method stub

	}

}
