package ml.core.gui.controls;

import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GuiControl extends GuiElement {
	
	public boolean enabled = true;

	public GuiControl(GuiElement parent, Vector2i position, Vector2i size) {
		super(parent);
		this.setLocalPosition(position);
		this.setSize(size);
	}

	/**
	 * When inheriting through GuiControl, the matrix will already be localized to your control's position
	 */
	@SideOnly(Side.CLIENT)
	public void drawBackground() {}

	/**
	 * When inheriting through GuiControl, the matrix will already be localized to your control's position
	 */
	@SideOnly(Side.CLIENT)
	public void drawOverlay() {}
	
	/**
	 * Always make a super call or a call to drawChilds() as your last call. It will render children.<br/>
	 * Your matrix will be localized to the parent element, so you need to shift by your local position.</br>
	 * You can also just override draw[Background|Overlay]() instead
	 */
	@SideOnly(Side.CLIENT)
	public void drawElement(RenderStage stage) {
		GL11.glPushMatrix();
		GL11.glTranslatef(getLocalPosition().x, getLocalPosition().y, 0);
		switch (stage) {
		case Background:
			drawBackground();
			break;
		//case Foreground:
			//drawForeground();
			//break;
		case Overlay:
			drawOverlay();
			break;
		}
		GL11.glPopMatrix();
		drawChilds(stage);
	}
}
