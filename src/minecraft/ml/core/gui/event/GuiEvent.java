package ml.core.gui.event;

import ml.core.gui.core.GuiElement;

public abstract class GuiEvent {
	
	public final GuiElement origin;
	public boolean propogate = true;
	public boolean cancelled = false;
	
	public GuiEvent(GuiElement origin) {
		this.origin = origin;
	}
	
	public void stopPropogation() {propogate = false;}
	public void cancel() {cancelled = true;}

}
