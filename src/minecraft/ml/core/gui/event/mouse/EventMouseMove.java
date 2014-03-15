package ml.core.gui.event.mouse;

import ml.core.gui.core.GuiElement;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;

public class EventMouseMove extends GuiEvent {

	public final Vector2i delta;
	
	public EventMouseMove(GuiElement origin, Vector2i delta) {
		super(origin);
		this.delta = delta;
	}

}
