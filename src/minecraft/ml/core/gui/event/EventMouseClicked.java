package ml.core.gui.event;

import ml.core.enums.MouseButton;
import ml.core.gui.core.GuiElement;

public class EventMouseClicked extends GuiEvent {
	
	public final int x;
	public final int y;
	public final MouseButton button;
	
	public EventMouseClicked(GuiElement origin, int x, int y, MouseButton btn) {
		super(origin);
		this.x = x;
		this.y = y;
		this.button = btn;
	}
	
}
