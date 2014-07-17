package ml.core.gui.core;

import java.util.ArrayList;
import java.util.List;

import ml.core.gui.core.style.GuiStyle;
import ml.core.gui.core.style.GuiStyleManip;
import ml.core.gui.event.EventFocusLost;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GuiElement {

	public GuiElement parentObject;
	protected List<GuiElement> childObjects = new ArrayList<GuiElement>();
	private Vector2i position;
	private Vector2i size;
	private boolean visible = true;
	
	/**
	 * Use {@link GuiElement#getStyle()} for getting. It checks for null and defaults to the parent.
	 */
	public GuiStyle style;
	
	public GuiElement(GuiElement parent) {
		parentObject = parent;
		if (parent != null)
			parent.addChild(this);
		setLocalPosition(new Vector2i());
		setSize(new Vector2i());
	}

	@SideOnly(Side.CLIENT)
	public void constructClient() {
		for (GuiElement elm : childObjects) {
			elm.constructClient();
		}
	}
	
	// ------------------------ Tree Model ------------------------ //
	public void clearChildren() {
		childObjects.clear();
	}
	
	public void removeChild(GuiElement chld) {
		if (childObjects.contains(chld))
			childObjects.remove(chld);
	}
	
	public void addChild(GuiElement chld) {
		if (!childObjects.contains(chld))
			childObjects.add(chld);
		chld.parentObject = this;
	}
	
	public GuiElement getParent() {
		return parentObject;
	}
	
	public boolean isTopParentElem() {
		return this instanceof TopParentGuiElement && getParent()==null;
	}
	
	public TopParentGuiElement getTopParent() {
		if (isTopParentElem()) return (TopParentGuiElement)this;
		if (getParent() == null) return null;
		return getParent().getTopParent();
	}
	
	public List<GuiElement> getAncestors() {
		List<GuiElement> lst = new ArrayList<GuiElement>();
		if (getParent() != null) {
			lst.add(getParent());
			lst.addAll(getParent().getAncestors());
		}
		return lst;
	}
	
	public List<GuiElement> getDescendants() {
		List<GuiElement> lst = new ArrayList<GuiElement>();
		for (GuiElement c : childObjects) {
			lst.add(c);
			lst.addAll(c.getDescendants());
		}
		return lst;
	}
	
	/**
	 * Returns whether the supplied element is an ancestor of this element
	 * @param ans
	 * @return
	 */
	public boolean isAncestor(GuiElement ans) {
		return ans==getParent() || (getParent() != null && getParent().isAncestor(ans));
	}
	
	public boolean isDescendant(GuiElement des) {
		return des.isAncestor(this);
	}
	
	/**
	 * @return The Side the element is instantiated on
	 */
	public Side getSide() {
		return getTopParent().getSide();
	}

	// ------------------------ Visibility Stuff ------------------------ //
	
	public void setVisibility(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public boolean isChildVisible(GuiElement elm) {
		return elm.isVisible() && ((getParent() != null && getParent().isChildVisible(this)) || isTopParentElem());
	}
	
	// ------------------------ Size Stuff ------------------------ //
	public Vector2i getSize() { return size; }
	
	public void setSize(Vector2i size) {
		this.size = size;
	}
	
	public void setSize(int w, int h) {
		setSize(new Vector2i(w, h));
	}
	
	public Rectangle calculateControlBox() {
		int minx=Integer.MAX_VALUE, miny=Integer.MAX_VALUE, maxw=0, maxh=0;
		
		for (GuiElement elm : childObjects) {
			minx = Math.min(minx, elm.getLocalPosition().x);
			miny = Math.min(miny, elm.getLocalPosition().y);
			maxw = Math.max(maxw, elm.getSize().x);
			maxh = Math.max(maxh, elm.getSize().y);
		}
		
		return new Rectangle(minx, miny, maxw, maxh);
	}
	
	// ------------------------ Position Stuff ------------------------ //
	public Vector2i getLocalPosition() {
		return position;
	}

	public void setLocalPosition(Vector2i position) {
		this.position = position;
	}
	
	public Vector2i getGlobalPosition() {
		if (!isTopParentElem()) return getParent().getGlobalPosition().add(getLocalPosition());
		return getLocalPosition().copy();
	}
	
	@SideOnly(Side.CLIENT)
	public Vector2i getLocalMousePos() {
		return localizeGlobalPos(getTopParent().gmousePos);
	}
	
	/**
	 * Localizes a point that is not localized.
	 */
	public Vector2i localizeGlobalPos(Vector2i g) {
		return g.copy().minus(getGlobalPosition());
	}
	
	/**
	 * Localizes a point that is presently localized to the parent element.
	 */
	public Vector2i localizeParentPos(Vector2i g) {
		return g.copy().minus(getLocalPosition());
	}
	
	public Rectangle getLocalBounds() {
		Vector2i pos = getLocalPosition();
		Vector2i sz = getSize();
		return new Rectangle(pos.x, pos.y, sz.x, sz.y);
	}
	
	public Rectangle getGlobalBounds() {
		Vector2i pos = getGlobalPosition();
		Vector2i sz = getSize();
		return new Rectangle(pos.x, pos.y, sz.x, sz.y);
	}
	
	public GuiEvent injectEvent(GuiEvent evt, boolean injectAtTop) {
		if (injectAtTop) {
			return getTopParent().injectEvent(evt, false);
		} else {
			onReceiveEvent(evt);
		}
		return evt;
	}
	
	public GuiEvent injectEvent(GuiEvent evt) { return injectEvent(evt, true); }
	
	/**
	 * First receiver and relayer of events. Should only be overridden in very special cases.
	 * Use {@link #handleEvent(GuiEvent)} instead
	 * @param evt
	 */
	public void onReceiveEvent(GuiEvent evt) {
		handleEvent(evt);
		
		if (evt.propogate) {
			for (GuiElement el : childObjects) {
				el.onReceiveEvent(evt);
			}
		}
	}
	
	public GuiElement findElementAtLocal(Vector2i pos) {
		for (GuiElement el : childObjects) {
			if (isChildVisible(el) && el.pointInElement(pos)) {
				GuiElement sel = el.findElementAtLocal(pos.copy().minus(el.getLocalPosition()));
				if (sel != null)
					return sel;
			}
		}
		return this;
	}
	
	/**
	 * Point is localized to the parent element
	 */
	public boolean pointInElement(Vector2i pos) {
		return (pos.x>=getLocalPosition().x && pos.y>=getLocalPosition().y &&
				pos.x<=getLocalPosition().x+getSize().x && pos.y<=getLocalPosition().y+getSize().y);
	}
	
	public void handleEvent(GuiEvent evt) {}
	
	/**
	 * Steal the focus from the currently focused element.
	 * Will fire {@link EventFocusLost} at the currently focused element if it is not null.
	 */
	public void takeFocus() {
		TopParentGuiElement top = getTopParent();
		if (top.focusedElement != null)
			injectEvent(new EventFocusLost(top.focusedElement));
		top.focusedElement = this;
	}
	
	/**
	 * Remove focus from <b>this</b> element, leaving no element in focus
	 */
	public void dropFocus() {
		TopParentGuiElement top = getTopParent();
		if (top.focusedElement == this) {
			injectEvent(new EventFocusLost(top.focusedElement));
			top.focusedElement = null;
		}
	}
	
	public boolean hasFocus() {
		return getTopParent().focusedElement == this;
	}
	
	public boolean hasHover() {
		return getTopParent().hoverElement == this;
	}
	
	/**
	 * Determine whether this element or any of its children is the hoverElement
	 */
	public boolean treeHasHover() {
		return hasHover() || (getTopParent().hoverElement != null && getTopParent().hoverElement.isAncestor(this));
	}
	
	/**
	 * The equivalent of {@link GuiScreen#updateScreen()}. Will only be called client-side, once per tick
	 */
	public void guiTick() {
		for (GuiElement el : childObjects) {
			el.guiTick();
		}
	}
	
	/**
	 * Gets a texture resource from the current style and binds it.
	 * @param feature
	 */
	protected void bindStyleTexture(String feature) {
		bindTexture(getStyle().getResource(feature));
	}
	
	protected void bindTexture(ResourceLocation res) {
		getTopParent().getGui().getMinecraft().getTextureManager().bindTexture(res);
	}

	/*
	 * NB: These aren't necessary, but are nice for clean subclass code.
	 */
	
	/**
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.
	 * @param partialTick TODO
	 */
	@SideOnly(Side.CLIENT)
	public void drawBackground(float partialTick) {}
	
	/**
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.
	 * @param partialTick TODO
	 */
	@SideOnly(Side.CLIENT)
	public void drawForeground(float partialTick) {}
	
	/**
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.
	 * @param partialTick TODO
	 */
	@SideOnly(Side.CLIENT)
	public void drawOverlay(float partialTick) {}
	
	@SideOnly(Side.CLIENT)
	public void drawTooltip(int mX, int mY, float partialTick) {}
	
	/**
	 * Always make a super call or a call to drawChilds() as your last call. It will render children.<br/>
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.</br>
	 * You can also just override draw[Background|Overlay]() instead
	 * @param partialTick TODO
	 */
	@SideOnly(Side.CLIENT)
	public void drawElement(RenderStage stage, float partialTick) {
		GL11.glPushMatrix();
		switch (stage) {
		case Background:
			drawBackground(partialTick);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			drawForeground(partialTick);
			break;
		case Overlay:
			drawOverlay(partialTick);
			break;
		}
		GL11.glPopMatrix();
		drawChilds(stage, partialTick);
	}
	
	/**
	 * Called to draw children of the element. Automatically called if you don't
	 * override {@link #drawElement(RenderStage, float)} or if you include a super call in your overriding method
	 * @param stage
	 */
	@SideOnly(Side.CLIENT)
	protected void drawChilds(RenderStage stage, float partialTick) {
		Vector2i pos = getLocalPosition();
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0.0F);
		for (GuiElement el : childObjects) {
			if (isChildVisible(el)) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
	
				el.drawElement(stage, partialTick);
			}
		}
		GL11.glPopMatrix();
	}
	
	@SideOnly(Side.CLIENT)
	public GuiStyle getStyle() {
		if (style == null) return getParent().getStyle();
		return style;
	}
	
	/**
	 * Sets the style of this element and any child elements with the same style.
	 * @param stl
	 */
	@SideOnly(Side.CLIENT)
	public void setStyle(GuiStyle stl) {
		for (GuiElement element : childObjects) {
			if (element.style == null || element.style == this.style) {
				element.setStyle(stl);
			}
		}
		this.style = stl;
	}
	
	/**
	 * Creates a {@link GuiStyleManip} and assigns it to this element if the current style is a {@link GuiStyleManip}
	 * Adds the resource to the {@link GuiStyleManip}
	 * @param feat
	 * @param npath
	 */
	@SideOnly(Side.CLIENT)
	public void setCustomResource(String feat, String npath) {
		if (!(this.style instanceof GuiStyleManip))
			this.style = new GuiStyleManip(getStyle());
		
		((GuiStyleManip)style).addResourceOverride(feat, npath);
	}
	
	/**
	 * Creates a {@link GuiStyleManip} and assigns it to this element if the current style is a {@link GuiStyleManip}
	 * Adds the color to the {@link GuiStyleManip}
	 * @param color Is expected to be a link to another color, a hex string prefixed with # (#RRGGBB), or an integer
	 */
	@SideOnly(Side.CLIENT)
	public void setCustomColor(String feat, String color) {
		if (!(this.style instanceof GuiStyleManip))
			this.style = new GuiStyleManip(style);
		((GuiStyleManip)style).addColorOverride(feat, color);
	}
	
	@SideOnly(Side.CLIENT)
	public MLGuiClient getGui() {
		return getParent().getGui();
	}
	
	@SideOnly(Side.CLIENT)
	public Minecraft getMC() {
		return getGui().getMinecraft();
	}
	
	@SideOnly(Side.CLIENT)
	public static enum RenderStage {
		Background,
		Overlay,
		SlotInventory;
	}
	
}
