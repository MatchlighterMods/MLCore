package ml.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.geo.GeoMath;
import net.minecraft.client.gui.Gui;

@SideOnly(Side.CLIENT)
public abstract class GuiControl extends GuiComponent {

	public int X;
	public int Y;
	
	public int width;
	public int height;
	
	public boolean enabled = true;
	
	public GuiControl(GuiContainerControl assocGui, int xPos, int yPos, int w, int h) {
		super(assocGui);
		X=xPos;
		Y=yPos;
		width=w;
		height=h;

	}
	
	@Override
	public boolean pointIn(int pX, int pY) {
		return GeoMath.pointInRect(pX, pY, X, Y, width, height);
	}
}
