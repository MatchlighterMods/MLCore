package ml.core.gui.event.mouse;

import ml.core.enums.MouseButton;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class EventMouseDown extends EventMousePositioned {
	
	public final MouseButton button;
	
	public EventMouseDown(GuiElement origin, Vector2i m, MouseButton btn) {
		super(origin, m);
		this.button = btn;
	}
}
