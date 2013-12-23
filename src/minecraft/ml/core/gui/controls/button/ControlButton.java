package ml.core.gui.controls.button;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.gui.event.EventButtonPressed;
import ml.core.gui.event.EventMouseClicked;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;
import net.minecraft.client.gui.FontRenderer;

public class ControlButton extends GuiControl {

	public String text;

	public ControlButton(GuiElement par, Vector2i pos, Vector2i size, String t) {
		super(par, pos, size);
		text = t;
	}

	@Override
	public void drawBackground() {
		bindStyleTexture("button");
		int p = enabled ? (hasHover() ? 40 : 20) : 0;
		GuiRenderUtils.drawSlicedRect(getPosition().x, getPosition().y, getSize().x, getSize().y, 0, p, 200, 20, 2, 2, 2, 2, 200, 80);

		FontRenderer fr = getGui().getMinecraft().fontRenderer;
		int l = getStyle().getColorValue(enabled ? (hasHover() ? "button.text.hover" : "button.text.normal") : "button.text.disabled");

		fr.drawStringWithShadow(text, getPosition().x + (getSize().x - fr.getStringWidth(text)) / 2, getPosition().y + (getSize().y - 8) / 2, l);
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt.origin == this && evt instanceof EventMouseClicked) {
			injectEvent(new EventButtonPressed(this, ((EventMouseClicked)evt).button));
		}
		super.handleEvent(evt);
	}
}
