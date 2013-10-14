package ml.core.gui.event;

import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class EventMouseMove extends GuiEvent {

	public final Vector2i delta;
	
	public EventMouseMove(GuiElement origin, Vector2i delta) {
		super(origin);
		this.delta = delta;
	}

}
