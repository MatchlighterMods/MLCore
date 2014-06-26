package ml.core.gui.event.mouse;

import ml.core.enums.MouseButton;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class EventMouseClicked extends EventMousePositioned {
	public final MouseButton button;
	public final int msLeng;
	
	public EventMouseClicked(GuiElement origin, Vector2i m, MouseButton btn, int msLength) {
		super(origin, m);
		this.button = btn;
		this.msLeng = msLength;
	}
}
