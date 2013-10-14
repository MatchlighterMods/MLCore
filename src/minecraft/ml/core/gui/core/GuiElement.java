package ml.core.gui.core;

import java.util.ArrayList;
import java.util.List;

import ml.core.gui.event.EventFocusLost;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GuiElement {

	public GuiElement parentObject;
	private List<GuiElement> childObjects = new ArrayList<GuiElement>();
	private Vector2i position;
	private Vector2i size;
	
	public GuiElement(GuiElement parent) {
		parentObject = parent;
		if (parent != null)
			parent.addChild(this);
		setPosition(new Vector2i());
		setSize(new Vector2i());
	}
	
	public Vector2i getPosition() {
		return position;
	}

	public void setPosition(Vector2i position) {
		this.position = position;
	}

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
	
	public Vector2i getSize() { return size; }
	
	public void setSize(Vector2i size) {
		this.size = size;
	}
	
	public void setSize(int w, int h) {
		setSize(new Vector2i(w, h));
	}

	public Vector2i getLocalPosition() {
		return getPosition();
	}
	
	public Vector2i getAbsolutePosition() {
		if (!isTopParentElem()) return getParent().getAbsolutePosition().add(getLocalPosition());
		return getLocalPosition().copy();
	}
	
	public Vector2i getLocalMousePos() {
		return localizeGlobal(getTopParent().gmousePos);
	}
	
	public Vector2i localizeGlobal(Vector2i g) {
		return g.copy().minus(getAbsolutePosition());
	}
	
	/**
	 * Localizes a point that is presently localized to the parent element.
	 */
	public Vector2i localizeParent(Vector2i g) {
		return g.copy().minus(getLocalPosition());
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
	
	public void onReceiveEvent(GuiEvent evt) {
		handleEvent(evt);
		
		if (evt.propogate) {
			for (GuiElement el : childObjects) {
				el.onReceiveEvent(evt);
			}
		}
	}
	
	public GuiElement findElementAt(Vector2i pos) {
		for (GuiElement el : childObjects) {
			if (el.pointInElement(pos)) {
				GuiElement sel = el.findElementAt(pos.copy().minus(getLocalPosition()));
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
	
	public void takeFocus() {
		TopParentGuiElement top = getTopParent();
		if (top.focusedElement != null)
			injectEvent(new EventFocusLost(top.focusedElement));
		top.focusedElement = this;
	}
	
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
	
	public void guiTick() {
		for (GuiElement el : childObjects) {
			el.guiTick();
		}
	}
	
	protected void bindTexture(ResourceLocation res) {
		getTopParent().getGui().getMinecraft().getTextureManager().bindTexture(res);
	}

	/**
	 * Always make a super call as your last call. It will render children.<br/>
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.
	 */
	@SideOnly(Side.CLIENT)
	public void drawBackground() {
		for (GuiElement el : childObjects) {
			Vector2i pos = getLocalPosition();
			GL11.glPushMatrix();
			GL11.glTranslatef(pos.x, pos.y, 0.0F);
			el.drawBackground();
			GL11.glPopMatrix();
		}
	}
	
	/**
	 * Always make a super call as your last call. It will render children.<br/>
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.
	 */
	@SideOnly(Side.CLIENT)
	public void drawForeground() {
		for (GuiElement el : childObjects) {
			Vector2i pos = getLocalPosition();
			GL11.glPushMatrix();
			GL11.glTranslatef(pos.x, pos.y, 0.0F);
			el.drawForeground();
			GL11.glPopMatrix();
		}
	}
	
	/**
	 * Always make a super call as your last call. It will render children.<br/>
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.
	 */
	@SideOnly(Side.CLIENT)
	public void drawOverlay() {
		for (GuiElement el : childObjects) {
			Vector2i pos = getLocalPosition();
			GL11.glPushMatrix();
			GL11.glTranslatef(pos.x, pos.y, 0.0F);
			el.drawOverlay();
			GL11.glPopMatrix();
		}
	}
	
}
