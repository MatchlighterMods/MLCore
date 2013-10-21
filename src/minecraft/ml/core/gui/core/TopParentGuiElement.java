package ml.core.gui.core;

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
	
	public EntityPlayer player;
	
	protected Side side;
	protected MLContainer container;
	@SideOnly(Side.CLIENT)
	protected MLGuiClient gui;
	
	public Vector2i gmousePos = new Vector2i();
	public GuiElement hoverElement;
	public GuiElement focusedElement;

	public TopParentGuiElement(EntityPlayer epl, Side side) {
		super(null);
		this.side = side;
		this.player = epl;
		container = new MLContainer(this);
		if (side == Side.CLIENT) {
			gui = new MLGuiClient(this);
		}
	}
	
	@Override
	public Vector2i getPosition() {
		if (gui == null) return super.getPosition();
		return gui.getPosition();
	}
	
	/**
	 * This will cause a refresh of the Gui (x|y)Size and position fields
	 */
	public void refreshGuiSize() {
		if (side == Side.CLIENT)
			getGui().refreshSize();
	}
	
	public void setSize(Vector2i sz) {
		super.setSize(sz);
		refreshGuiSize();
	}
	
	public Side getSide() {
		return side;
	}
	
	@SideOnly(Side.CLIENT)
	public MLGuiClient getGui() {
		return gui;
	}

	public MLContainer getContainer() {
		return container;
	}
	
	public abstract void initControls();
	
	public abstract boolean canInteractWith(EntityPlayer epl);
}
