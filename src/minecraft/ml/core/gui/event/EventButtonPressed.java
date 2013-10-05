package ml.core.gui.event;

import ml.core.enums.MouseButton;
import ml.core.gui.controls.button.ControlButton;

public class EventButtonPressed extends GuiEvent {
	
	public MouseButton button;

	public EventButtonPressed(ControlButton origin, MouseButton btn) {
		super(origin);
		button=btn;
	}

}
