package ml.core.gui.event;

import ml.core.enums.MouseButton;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class EventMouseDown extends GuiEvent {
	
	public final Vector2i mPos;
	public final MouseButton button;
	
	public EventMouseDown(GuiElement origin, Vector2i m, MouseButton btn) {
		super(origin);
		this.mPos = m;
		this.button = btn;
	}
	
	public Vector2i localizePosition() {
		return origin.localizeGlobalPos(mPos);
	}
}
