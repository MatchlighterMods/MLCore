package ml.core.gui.controls;

import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public abstract class GuiControl extends GuiElement {
	
	public boolean enabled = true;

	public GuiControl(GuiElement parent, Vector2i position, Vector2i size) {
		super(parent);
		this.setPosition(position);
		this.setSize(size);
	}
		
}
