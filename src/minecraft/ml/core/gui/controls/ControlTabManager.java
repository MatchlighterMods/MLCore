package ml.core.gui.controls;

import java.util.ArrayList;
import java.util.List;

import ml.core.geo.Rectangle;
import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiControl;
import ml.core.gui.GuiSide;
import ml.core.gui.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ControlTabManager extends GuiControl {

	public abstract static class GuiTab extends Gui {

		public ControlTabManager TabManager;

		public GuiTab(ControlTabManager ctm) {
			TabManager = ctm;
		}

		public int tabColor = 0x3590FF;
		
		public abstract void renderContents(Minecraft mc, int mX, int mY);
		public abstract int getWidth();
		public abstract int getHeight();
		
		public boolean onMouseClicked(int mouseX, int mouseY, MouseButton button) {
			return true;
		}
		
		public void onMouseMovedOrUp(int mX, int mY, int act) {}
		
		public boolean onKeyPress(char chr, int key) {
			return false;
		}

		public void renderAt(Minecraft mc, int drawX, int drawY, int mX, int mY) {
			GL11.glPushMatrix();
			renderBackground(mc, drawX, drawY, mX, mY);
			GL11.glTranslatef(drawX, drawY, 0F);
			renderContents(mc, mX, mY);
			GL11.glPopMatrix();
		}

		protected void renderBackground(Minecraft mc, int drawX, int drawY, int mX, int mY) {
			float red = ((tabColor >> 16) & 0xFF) /255F;
			float green = ((tabColor >> 8) & 0xFF) /255F;
			float blue = (tabColor & 0xFF) /255F;

			GL11.glColor4f(red, green, blue, 1.0F);
			mc.renderEngine.bindTexture("mods/MLCore/textures/gui/ledger.png");

			switch (TabManager.side){
			case Left:
				this.drawTexturedModalRect(drawX, drawY, 0, 0, this.getWidth(), this.getHeight()-4);
				this.drawTexturedModalRect(drawX, drawY+4, 0, 256-this.getHeight()+4, this.getWidth(), this.getHeight()-4);
				break;
			case Right:
				this.drawTexturedModalRect(drawX, drawY, 256-this.getWidth(), 0, this.getWidth(), this.getHeight()-4);
				this.drawTexturedModalRect(drawX, drawY+4, 256-this.getWidth(), 256-this.getHeight()+4, this.getWidth(), this.getHeight()-4);
				break;
			case Top:
				this.drawTexturedModalRect(drawX, drawY, 0, 0, this.getWidth()-4, this.getHeight());
				this.drawTexturedModalRect(drawX+4, drawY, 256-this.getWidth()+4, 0, this.getWidth()-4, this.getHeight());
				break;
			case Bottom:
				this.drawTexturedModalRect(drawX, drawY, 0, 256-this.getHeight(), this.getWidth()-4, this.getHeight());
				this.drawTexturedModalRect(drawX+4, drawY, 256-this.getWidth()+4, 256-this.getHeight(), this.getWidth()-4, this.getHeight());
				break;
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
		return getTabAt(pX, pY) != null;
	}
	
	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, MouseButton button) {
		for (GuiTab gt : tabs) {
			Rectangle tb = getTabBounds(gt);
			if (gt.onMouseClicked(mouseX-tb.xCoord, mouseY-tb.yCoord, button))
				return true;
		}
		return false;
	}
	
	@Override
	public void onMouseMovedOrUp(int mX, int mY, int act) {
		for (GuiTab gt : tabs) {
			Rectangle tb = getTabBounds(gt);
			gt.onMouseMovedOrUp(mX-tb.xCoord, mY-tb.yCoord, act);
		}
	}
	
	@Override
	public boolean onKeyPress(char chr, int key) {
		for (GuiTab gt : tabs) {
			if (gt.onKeyPress(chr, key))
				return true;
		}
		return false;
	}

	@Override
	public void performRender(Minecraft mc, int mouseX, int mouseY) {
		int offset = 4;
		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				gt.renderAt(mc, side==GuiSide.Left ? -gt.getWidth() : guiContainer.getWinWidth(), offset, mouseX, mouseY);
				offset += gt.getHeight();
				break;
			case Top:
			case Bottom:
				gt.renderAt(mc, side==GuiSide.Top ? -gt.getHeight() : guiContainer.getWinHeight(), offset, mouseX, mouseY);
				offset =+ gt.getWidth();
				break;	
			}
		}
	}

	public Rectangle getTabBounds(GuiTab iTab) {
		int offset = 4;
		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				if (gt==iTab)
					return new Rectangle(side==GuiSide.Left ? -gt.getWidth() : guiContainer.getWinWidth(), offset, gt.getWidth(), gt.getHeight());
				offset += gt.getHeight();
				break;
			case Top:
			case Bottom:
				if (gt==iTab)
					return new Rectangle(side==GuiSide.Top ? -gt.getHeight() : guiContainer.getWinHeight(), offset, gt.getWidth(), gt.getHeight());
				offset =+ gt.getWidth();
				break;	
			}
		}
		return new Rectangle(0, 0, 0, 0);
	}
	
	public GuiTab getTabAt(int x, int y) {
		int xLower = 4;
		int yLower = 4;

		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				if (y>yLower && y<yLower+gt.getHeight() &&
						((side==GuiSide.Left && x>-gt.getWidth() && x<0) ||
								(side==GuiSide.Right && x>guiContainer.getWinWidth() && x<guiContainer.getWinWidth()+gt.getWidth()))) {
					return gt;
				}
				yLower += gt.getHeight();
				break;
			case Top:
			case Bottom:
				if (x>xLower && y<xLower+gt.getWidth() &&
						((side==GuiSide.Top && y>-gt.getHeight() && y<0) ||
								(side==GuiSide.Bottom && y>guiContainer.getWinHeight() && y<guiContainer.getWinHeight()+gt.getHeight()))) {
					return gt;
				}
				xLower += gt.getWidth();
				break;
			}
		}
		return null;
	}

}
