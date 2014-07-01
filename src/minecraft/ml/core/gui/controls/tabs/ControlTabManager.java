package ml.core.gui.controls.tabs;

import java.util.ArrayList;
import java.util.List;

import ml.core.enums.NaturalSide;
import ml.core.gui.GuiRenderUtils;
import ml.core.gui.core.GuiElement;
import ml.core.gui.core.Window;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;

import org.lwjgl.opengl.GL11;

public class ControlTabManager extends GuiElement {
	
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
		public int sizingSpeed = 32;
		
		public GuiTab(ControlTabManager ctm) {
			super(ctm);
			TabManager = ctm;
			setSize(new Vector2i(defaultSize, defaultSize));
		}

		@Override
		public void constructClient() {
			setSize(getTargetSize());
			super.constructClient();
		}
		
		public int tabColor = 0x3590FF;
		
		@Override
		public void guiTick() {
			super.guiTick();
			
			Vector2i trg = getTargetSize();
			lastSize = new Vector2i(getSize());
			getSize().x = Math.abs(getSize().x-trg.x) < sizingSpeed ? trg.x : getSize().x + (getSize().x > trg.x ? -sizingSpeed : sizingSpeed);
			getSize().y = Math.abs(getSize().y-trg.y) < sizingSpeed ? trg.y : getSize().y + (getSize().y > trg.y ? -sizingSpeed : sizingSpeed);
		}
		
		public Vector2i getTargetSize() {
			return getSize();
		}
		
		@Override
		public Vector2i getLocalPosition() {
			Rectangle tb = TabManager.getTabBounds(this);
			return new Vector2i(tb.xCoord, tb.yCoord);
		}

		private Vector2i lastSize = new Vector2i();
		@Override
		public void drawBackground(float partialTick) {
			getLocalPosition().glTranslate();
			float red = ((tabColor >> 16) & 0xFF) /255F;
			float green = ((tabColor >> 8) & 0xFF) /255F;
			float blue = (tabColor & 0xFF) /255F;

			GL11.glColor4f(red, green, blue, 1.0F);
			bindTexture(getStyle().getResource("ledger"));
			
			int sizeX = lastSize.x + (int)((getSize().x - lastSize.x) * partialTick);
			int sizeY = lastSize.y + (int)((getSize().y - lastSize.y) * partialTick);
			
			switch (TabManager.side){
			case Left:
				GuiRenderUtils.drawTexturedModalRect(0, 0, 0, 0, sizeX, sizeY-4);
				GuiRenderUtils.drawTexturedModalRect(0, 4, 0, 256-sizeY+4, sizeX, sizeY-4);
				break;
			case Right:
				GuiRenderUtils.drawTexturedModalRect(0, 0, 256-sizeX, 0, sizeX, sizeY-4);
				GuiRenderUtils.drawTexturedModalRect(0, 4, 256-sizeX, 256-sizeY+4, sizeX, sizeY-4);
				break;
			case Top:
				GuiRenderUtils.drawTexturedModalRect(0, 0, 0, 0, sizeX-4, sizeY);
				GuiRenderUtils.drawTexturedModalRect(4, 0, 256-sizeX+4, 0, sizeX-4, sizeY);
				break;
			case Bottom:
				GuiRenderUtils.drawTexturedModalRect(0, 0, 0, 256-sizeY, sizeX-4, sizeY);
				GuiRenderUtils.drawTexturedModalRect(4, 0, 256-sizeX+4, 256-sizeY, sizeX-4, sizeY);
				break;
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
	
	public Rectangle getTabBounds(GuiTab iTab) {
		int offset = tabMargin;
		for (GuiTab gt : tabs) {
			switch (side) {
			case Left:
			case Right:
				if (gt==iTab)
					return new Rectangle(side==NaturalSide.Left ? -gt.getSize().x : cgui.getSize().x, offset, gt.getSize().x, gt.getSize().y);
				offset += gt.getSize().y;
				break;
			case Top:
			case Bottom:
				if (gt==iTab)
					return new Rectangle(side==NaturalSide.Top ? -gt.getSize().y : cgui.getSize().y, offset, gt.getSize().x, gt.getSize().y);
				offset =+ gt.getSize().x;
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
				if (pos.y>yLower && pos.y<yLower+gt.getSize().y &&
						((side==NaturalSide.Left && pos.x>-gt.getSize().x && pos.x<0) ||
								(side==NaturalSide.Right && pos.x>cgui.getSize().x && pos.x<cgui.getSize().x+gt.getSize().x))) {
					return gt;
				}
				yLower += gt.getSize().y;
				break;
			case Top:
			case Bottom:
				if (pos.x>xLower && pos.y<xLower+gt.getSize().x &&
						((side==NaturalSide.Top && pos.y>-gt.getSize().y && pos.y<0) ||
								(side==NaturalSide.Bottom && pos.y>cgui.getSize().y && pos.y<cgui.getSize().y+gt.getSize().y))) {
					return gt;
				}
				xLower += gt.getSize().x;
				break;
			}
		}
		return null;
	}

}
