package ml.core.gui.core;

import ml.core.gui.MLContainer;
import ml.core.gui.MLGuiClient;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * TopParentGuiObjects house all other GuiObjects in a Gui
 * May also be a child element
 * @author Matchlighter
 */
public abstract class TopParentGuiElement extends GuiElement {
	
	protected MLGuiClient gui;
	protected MLContainer container;
	public Vector2i gmousePos = new Vector2i();
	public GuiElement hoverElement;
	public GuiElement focusedElement;

	public TopParentGuiElement(EntityPlayer epl, Side side) {
		super(null);
		container = new MLContainer(this);
		if (side == Side.CLIENT) {
			gui = new MLGuiClient(this);
		}
	}
	
	@Override
	public Vector2i getLocalPosition() {
		if (gui == null) return super.getLocalPosition();
		return gui.getPosition();
	}
	
	@SideOnly(Side.CLIENT)
	public MLGuiClient getGui() {
		return gui;
	}

	public MLContainer getContainer() {
		return container;
	}
}
