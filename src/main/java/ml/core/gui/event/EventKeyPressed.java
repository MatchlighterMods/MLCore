package ml.core.gui.event;

import ml.core.gui.core.GuiElement;

public class EventKeyPressed extends GuiEvent {

	public char character;
	public int key;
	
	public EventKeyPressed(GuiElement origin, char chr, int key) {
		super(origin);
		this.character = chr;
		this.key = key;
	}

	
}
