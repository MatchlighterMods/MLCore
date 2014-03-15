package ml.core.gui.event.mouse;

import ml.core.enums.MouseButton;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class EventMouseUp extends EventMousePositioned {
	public final MouseButton button;
	
	public EventMouseUp(GuiElement origin, Vector2i m, MouseButton btn) {
		super(origin, m);
		this.button = btn;
	}
}
