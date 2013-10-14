package ml.core.gui.controls.button;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.gui.event.EventButtonPressed;
import ml.core.gui.event.EventMouseClicked;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ControlButton extends GuiControl {
	
	protected static final ResourceLocation buttonRes = new ResourceLocation("textures/gui/widgets.png");
	
	public String text;
	
	public ControlButton(GuiElement par, Vector2i pos, Vector2i size, String t) {
		super(par, pos, size);
		text = t;
	}
	
	@Override
	public void drawForeground() {
		getTopParent().getGui().getMinecraft().getTextureManager().bindTexture(buttonRes);
		Vector2i mPos = getLocalMousePos();
		GuiRenderUtils.drawSlicedRect(getPosition().x, getPosition().y, getSize().x, getSize().y, 0, enabled ? getTopParent().hoverElement==this ? 86 : 66 : 46, 200, 20, 2, 2, 2, 2);
		super.drawForeground();
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt.origin == this && evt instanceof EventMouseClicked) {
			injectEvent(new EventButtonPressed(this, ((EventMouseClicked)evt).button));
		}
		super.handleEvent(evt);
	}
}
