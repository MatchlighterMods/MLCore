package ml.core.gui.controls.box;

import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class ControlCenteredBox extends ControlBox {

	public boolean centerHorizontal, centerVertical;
	
	public ControlCenteredBox(GuiElement parent, Vector2i position, Vector2i size, boolean centerHorizontal, boolean centerVertical) {
		super(parent, position, size);
		this.centerHorizontal = centerHorizontal;
		this.centerVertical = centerVertical;
	}
	
	public ControlCenteredBox(GuiElement parent, Vector2i position, Vector2i size) {
		this(parent, position, size, true, true);
	}
	
	@Override
	public Vector2i getLocalPosition() {
		return new Vector2i(centerHorizontal ? (getParent().getSize().x - getSize().x) / 2 : super.getLocalPosition().x, centerVertical ? (getParent().getSize().y - getSize().y) / 2 : super.getLocalPosition().y);
	}

}
