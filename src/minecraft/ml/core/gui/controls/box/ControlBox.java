package ml.core.gui.controls.box;

import ml.core.gui.GuiRenderUtils;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class ControlBox extends GuiControl {

	public int backgroundColor = 0x44000000;
	
	public ControlBox(GuiElement parent, Vector2i position, Vector2i size) {
		super(parent, position, size);
	}

	@Override
	public void drawBackground(float partialTick) {
		GuiRenderUtils.drawGradientRect(0, 0, getSize().x, getSize().y, backgroundColor, backgroundColor);
	}
}
