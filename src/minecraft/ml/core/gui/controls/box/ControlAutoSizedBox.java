package ml.core.gui.controls.box;

import ml.core.gui.core.GuiElement;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;

public class ControlAutoSizedBox extends ControlCenteredBox {

	public ControlAutoSizedBox(GuiElement parent, Vector2i position, boolean centerHorizontal, boolean centerVertical) {
		super(parent, position, new Vector2i(), centerHorizontal, centerVertical);
	}

	@Override
	public Vector2i getSize() {
		Rectangle controlRect = calculateControlBox();
		return new Vector2i(controlRect.width + controlRect.xCoord*2,
				controlRect.height + controlRect.yCoord*2);
	}
	
}
