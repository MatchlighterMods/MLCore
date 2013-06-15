package ml.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.geo.GeoMath;
import ml.core.geo.Rectangle;
import net.minecraft.client.gui.Gui;

@SideOnly(Side.CLIENT)
public abstract class GuiPositionedControl extends GuiControl {

	public Rectangle bounds = new Rectangle(0, 0, 0, 0);
	
	public boolean enabled = true;
	
	public GuiPositionedControl(GuiContainerControl assocGui, int xPos, int yPos, int w, int h) {
		super(assocGui);
		
	}
	
	public void setBounds(Rectangle rect) {
		this.bounds = new Rectangle(rect);
	}
	
	public void setBounds(int xPos, int yPos, int w, int h) {
		bounds.xCoord = xPos;
		bounds.yCoord = yPos;
		bounds.width = w;
		bounds.height = h;
	}
	
	@Override
	public boolean isPointIn(int pX, int pY) {
		return GeoMath.pointInRect(pX, pY, bounds.xCoord, bounds.yCoord, bounds.width, bounds.height);
	}
}
