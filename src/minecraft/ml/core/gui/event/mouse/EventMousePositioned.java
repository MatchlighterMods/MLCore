package ml.core.gui.event.mouse;

import ml.core.gui.core.GuiElement;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;

public abstract class EventMousePositioned extends GuiEvent {
	public final Vector2i mPos;
	
	public EventMousePositioned(GuiElement origin, Vector2i m) {
		super(origin);
		this.mPos = m;
	}
	
	public Vector2i localizePosition() {
		return origin.localizeGlobalPos(mPos);
	}
}
