package ml.core.gui.controls;

import ml.core.gui.core.GuiElement;
import ml.core.gui.event.EventKeyPressed;
import ml.core.gui.event.EventMouseClicked;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;

public class ControlTextInput extends GuiControl {

	public String value;
	
	public ControlTextInput(GuiElement parent, Vector2i pos, Vector2i size) {
		super(parent, pos, size);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt.origin == this && evt instanceof EventMouseClicked) {
			takeFocus();
		} else if (hasFocus() && evt instanceof EventKeyPressed) {
			evt.cancel();
			EventKeyPressed kpevt = (EventKeyPressed)evt;
			// TODO Type
		}
		super.handleEvent(evt);
	}
}
