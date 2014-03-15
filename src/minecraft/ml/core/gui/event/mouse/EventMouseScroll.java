package ml.core.gui.event.mouse;

import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class EventMouseScroll extends EventMousePositioned {
	public final int scrollDelta;
	
	public EventMouseScroll(GuiElement origin, Vector2i m,  int sDelta) {
		super(origin, m);
		this.scrollDelta = sDelta;
	}
}
