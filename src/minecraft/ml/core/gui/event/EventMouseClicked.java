package ml.core.gui.event;

import ml.core.enums.MouseButton;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class EventMouseClicked extends GuiEvent {
	public final Vector2i mPos;
	public final MouseButton button;
	public final int msLeng;
	
	public EventMouseClicked(GuiElement origin, Vector2i m, MouseButton btn, int msLength) {
		super(origin);
		this.mPos = m;
		this.button = btn;
		this.msLeng = msLength;
	}
	
	public Vector2i localizePosition() {
		return origin.localizeGlobal(mPos);
	}
}
