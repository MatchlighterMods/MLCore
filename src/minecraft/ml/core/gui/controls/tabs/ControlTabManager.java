package ml.core.gui.controls.tabs;

import java.util.ArrayList;
import java.util.List;

import ml.core.enums.NaturalSide;
import ml.core.gui.core.GuiElement;
import ml.core.gui.core.Window;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ControlTabManager extends GuiElement {
	
	protected static ResourceLocation ledgerRes = new ResourceLocation("MLCore:textures/gui/ledger.png");
	
	/**
	 * The margin between Tabs and the edge of the Window
	 */
	public int tabMargin = 4;
	
	/**
	 * Note that all mouse coordinate parameters will be tab-localized.
	 */
	public abstract static class GuiTab extends GuiElement {

		public ControlTabManager TabManager;

		public static final int defaultSize = 24;
		public int sizingSpeed = 8;
		
		public GuiTab(ControlTabManager ctm) {
			super(ctm);
			TabManager = ctm;
			size = new Vector2i(defaultSize, defaultSize);
		}

		public int tabColor = 0x3590FF;
		
		@Override
		public void guiTick() {
			super.guiTick();
			
			Vector2i trg = getTargetSize();
			size.x = Math.abs(size.x-trg.x) < sizingSpeed ? trg.x : size.x + (size.x > trg.x ? -sizingSpeed : sizingSpeed);
			size.y = Math.abs(size.y-trg.y) < sizingSpeed ? trg.y : size.y + (size.y > trg.y ? -sizingSpeed : sizingSpeed);
		}
		
		public Vector2i getTargetSize() {
			return size;
		}
		
		@Override
		public Vector2i getLocalPosition() {
			Rectangle tb = TabManager.getTabBounds(this);
			return new Vector2i(tb.xCoord, tb.yCoord);
		}

		@Override
		public boolean pointInElement(Vector2i pos) {
			return TabManager.getTabAt(pos) == this;
		}
		
		@Override
		public void drawBackground() {
			float red = ((tabColor >> 16) & 0xFF) /255F;
			float green = ((tabColor >> 8) & 0xFF) /255F;
			float blue = (tabColor & 0xFF) /255F;

			GL11.glColor4f(red, green, blue, 1.0F);
			bindTexture(ledgerRes);

			switch (TabManager.side){
			case Left:
				this.drawTexturedModalRect(0, 0, 0, 0, this.size.x, this.size.y-4);
				this.drawTexturedModalRect(0, 4, 0, 256-this.size.y+4, this.size.x, this.size.y-4);
				break;
			case Right:
				this.drawTexturedModalRect(0, 0, 256-this.size.x, 0, this.size.x, this.size.y-4);
				this.drawTexturedModalRect(0, 4, 256-this.size.x, 256-this.size.y+4, this.size.x, this.size.y-4);
				break;
			case Top:
				this.drawTexturedModalRect(0, 0, 0, 0, this.size.x-4, this.size.y);
				this.drawTexturedModalRect(4, 0, 256-this.size.x+4, 0, this.size.x-4, this.size.y);
				break;
			case Bottom:
				this.drawTexturedModalRect(0, 0, 0, 256-this.size.y, this.size.x-4, this.size.y);
				this.drawTexturedModalRect(4, 0, 256-this.size.x+4, 256-this.size.y, this.size.x-4, this.size.y);
				break;
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			super.drawBackground();
		}
	}

	public Window cgui;
	public NaturalSide side;
	public List<GuiTab> tabs = new ArrayList<ControlTabManager.GuiTab>();

	public ControlTabManager(Window parent, NaturalSide sd) {
		super(parent);
		cgui = parent;
		side = sd;
	}
	
	@Override
	public void addChild(GuiElement chld) {
		if (chld instanceof GuiTab) {
			super.addChild(chld);
			tabs.add((GuiTab)chld);
		} else {
			throw new RuntimeException("Attempt to add a GuiElement not of type GuiTab to a tab manager");
		}
	}

	@Override
	public boolean pointInElement(Vector2i pos) {
		return getTabAt(pos) != null;
	}
	
	@Override
	public void guiTick() {
		super.guiTick();
	}
	
	@Override
	public void drawBackground() {
//		int offset = tabMargin;
//		Minecraft mc = getTopParent().getGui().getMinecraft();
//		for (GuiTab gt : tabs) {
//			switch (side) {
//			case Left:
//			case Right:
//				int x = side==NaturalSide.Left ? -gt.size.x : cgui.getSize().x;
//				gt.renderAt(mc, x, offset, mouseX-x, mouseY-offset);
//				offset += gt.size.y;
//				break;
//			case Top:
//			case Bottom:
//				int y = side==NaturalSide.Top ? -gt.size.y : cgui.getSize().y;
//				gt.renderAt(mc, offset, y, mouseX-offset, mouseY-y);
//				offset =+ gt.size.x;
//				break;	
//			}
//		}
		
		super.drawBackground();
	}

	public Rectangle getTabBounds(GuiTab iTab) {
		int offset = tabMargin;
		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				if (gt==iTab)
					return new Rectangle(side==NaturalSide.Left ? -gt.size.x : cgui.getSize().x, offset, gt.size.x, gt.size.y);
				offset += gt.size.y;
				break;
			case Top:
			case Bottom:
				if (gt==iTab)
					return new Rectangle(side==NaturalSide.Top ? -gt.size.y : cgui.getSize().y, offset, gt.size.x, gt.size.y);
				offset =+ gt.size.x;
				break;	
			}
		}
		return new Rectangle(0, 0, 0, 0);
	}
	
	public GuiTab getTabAt(Vector2i pos) {
		int xLower = tabMargin;
		int yLower = tabMargin;

		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				if (pos.y>yLower && pos.y<yLower+gt.size.y &&
						((side==NaturalSide.Left && pos.x>-gt.size.x && pos.x<0) ||
								(side==NaturalSide.Right && pos.x>cgui.getSize().x && pos.x<cgui.getSize().x+gt.size.x))) {
					return gt;
				}
				yLower += gt.size.y;
				break;
			case Top:
			case Bottom:
				if (pos.x>xLower && pos.y<xLower+gt.size.x &&
						((side==NaturalSide.Top && pos.y>-gt.size.y && pos.y<0) ||
								(side==NaturalSide.Bottom && pos.y>cgui.getSize().y && pos.y<cgui.getSize().y+gt.size.y))) {
					return gt;
				}
				xLower += gt.size.x;
				break;
			}
		}
		return null;
	}

}
