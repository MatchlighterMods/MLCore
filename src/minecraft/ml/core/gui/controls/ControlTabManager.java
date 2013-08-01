package ml.core.gui.controls;

import java.util.ArrayList;
import java.util.List;

import ml.core.geo.Rectangle;
import ml.core.geo.Vector2;
import ml.core.gui.GuiContainerControl;
import ml.core.gui.GuiControl;
import ml.core.gui.GuiSide;
import ml.core.gui.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ControlTabManager extends GuiControl {
	
	protected static ResourceLocation ledgerRes = new ResourceLocation("MLCore:textures/gui/ledger.png");
	
	public int tabTopMargin = 4;
	
	/**
	 * Note that all mouse coordinate parameters will be tab-localized.
	 */
	public abstract static class GuiTab extends Gui {

		public ControlTabManager TabManager;

		public static final int defaultSize = 24;
		public int sizingSpeed = 8;
		
		public Vector2<Integer> size;
		
		public GuiTab(ControlTabManager ctm) {
			TabManager = ctm;
			size = new Vector2<Integer>(defaultSize, defaultSize);
		}

		public int tabColor = 0x3590FF;
		
		public abstract void renderContents(Minecraft mc, int lmX, int lmY);
		
		public void updateTick() {
			Vector2<Integer> trg = getTargetSize();
			size.X = Math.abs(size.X-trg.X) < sizingSpeed ? trg.X : size.X + (size.X > trg.X ? -sizingSpeed : sizingSpeed);
			size.Y = Math.abs(size.Y-trg.Y) < sizingSpeed ? trg.Y : size.Y + (size.Y > trg.Y ? -sizingSpeed : sizingSpeed);
		}
		
		public Vector2<Integer> getTargetSize() {
			return size;
		}
		
		public List<String> getTooltipLines(int lmx, int lmy) {
			return new ArrayList<String>();
		}
		
		public boolean onMouseClicked(int lmX, int lmY, MouseButton button) {
			return true;
		}
		
		public void onMouseMovedOrUp(int lmX, int lmY, int act) {}
		
		public boolean onKeyPress(char chr, int key) {
			return false;
		}

		public void renderAt(Minecraft mc, int drawX, int drawY, int lmX, int lmY) {
			GL11.glPushMatrix();
			GL11.glTranslatef(drawX, drawY, 0F);
			
			renderBackground(mc, lmX, lmY);
			renderContents(mc, lmX, lmY);
			
			GL11.glPopMatrix();
		}

		protected void renderBackground(Minecraft mc, int lmX, int lmY) {
			float red = ((tabColor >> 16) & 0xFF) /255F;
			float green = ((tabColor >> 8) & 0xFF) /255F;
			float blue = (tabColor & 0xFF) /255F;

			GL11.glColor4f(red, green, blue, 1.0F);
			mc.func_110434_K().func_110577_a(ledgerRes);

			switch (TabManager.side){
			case Left:
				this.drawTexturedModalRect(0, 0, 0, 0, this.size.X, this.size.Y-4);
				this.drawTexturedModalRect(0, 4, 0, 256-this.size.Y+4, this.size.X, this.size.Y-4);
				break;
			case Right:
				this.drawTexturedModalRect(0, 0, 256-this.size.X, 0, this.size.X, this.size.Y-4);
				this.drawTexturedModalRect(0, 4, 256-this.size.X, 256-this.size.Y+4, this.size.X, this.size.Y-4);
				break;
			case Top:
				this.drawTexturedModalRect(0, 0, 0, 0, this.size.X-4, this.size.Y);
				this.drawTexturedModalRect(4, 0, 256-this.size.X+4, 0, this.size.X-4, this.size.Y);
				break;
			case Bottom:
				this.drawTexturedModalRect(0, 0, 0, 256-this.size.Y, this.size.X-4, this.size.Y);
				this.drawTexturedModalRect(4, 0, 256-this.size.X+4, 256-this.size.Y, this.size.X-4, this.size.Y);
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
	public List<String> getTooltipLines(int mx, int my) {
		GuiTab tab = getTabAt(mx, my);
		if (tab != null) {
			Rectangle bbox = getTabBounds(tab);
			return tab.getTooltipLines(mx-bbox.xCoord, my-bbox.yCoord);
		}
		return super.getTooltipLines(mx, my);
	}

	@Override
	public void update() {
		super.update();
		
		for (GuiTab gt : tabs) {
			gt.updateTick();
		}
	}
	
	@Override
	public void renderBackground(Minecraft mc, int mouseX, int mouseY) {
		int offset = tabTopMargin;
		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				int x = side==GuiSide.Left ? -gt.size.X : guiContainer.getWinWidth();
				gt.renderAt(mc, x, offset, mouseX-x, mouseY-offset);
				offset += gt.size.Y;
				break;
			case Top:
			case Bottom:
				int y = side==GuiSide.Top ? -gt.size.Y : guiContainer.getWinHeight();
				gt.renderAt(mc, offset, y, mouseX-offset, mouseY-y);
				offset =+ gt.size.X;
				break;	
			}
		}
	}

	public Rectangle getTabBounds(GuiTab iTab) {
		int offset = tabTopMargin;
		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				if (gt==iTab)
					return new Rectangle(side==GuiSide.Left ? -gt.size.X : guiContainer.getWinWidth(), offset, gt.size.X, gt.size.Y);
				offset += gt.size.Y;
				break;
			case Top:
			case Bottom:
				if (gt==iTab)
					return new Rectangle(side==GuiSide.Top ? -gt.size.Y : guiContainer.getWinHeight(), offset, gt.size.X, gt.size.Y);
				offset =+ gt.size.X;
				break;	
			}
		}
		return new Rectangle(0, 0, 0, 0);
	}
	
	public GuiTab getTabAt(int x, int y) {
		int xLower = tabTopMargin;
		int yLower = tabTopMargin;

		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				if (y>yLower && y<yLower+gt.size.Y &&
						((side==GuiSide.Left && x>-gt.size.X && x<0) ||
								(side==GuiSide.Right && x>guiContainer.getWinWidth() && x<guiContainer.getWinWidth()+gt.size.X))) {
					return gt;
				}
				yLower += gt.size.Y;
				break;
			case Top:
			case Bottom:
				if (x>xLower && y<xLower+gt.size.X &&
						((side==GuiSide.Top && y>-gt.size.Y && y<0) ||
								(side==GuiSide.Bottom && y>guiContainer.getWinHeight() && y<guiContainer.getWinHeight()+gt.size.Y))) {
					return gt;
				}
				xLower += gt.size.X;
				break;
			}
		}
		return null;
	}

}
