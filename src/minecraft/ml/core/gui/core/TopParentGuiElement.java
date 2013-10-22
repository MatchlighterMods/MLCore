package ml.core.gui.core;

import ml.core.gui.core.style.GuiStyle;
import ml.core.gui.core.style.StyleManager;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
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
	@SideOnly(Side.CLIENT)
	public final ResourceLocation controlSheet = new ResourceLocation("MLCore:textures/gui/controls.png");
	
	public Vector2i gmousePos = new Vector2i();
	public GuiElement hoverElement;
	public GuiElement focusedElement;

	public TopParentGuiElement(EntityPlayer epl, Side side) {
		super(null);
		this.side = side;
		this.player = epl;
		this.container = new MLContainer(this);
		if (side == Side.CLIENT) {
			this.gui = new MLGuiClient(this);
			this.style = StyleManager.defaultStyle;
		}
	}
	
	@Override
	public Vector2i getPosition() {
		if (gui == null) return super.getPosition();
		return gui.getPosition();
	}
	
	@Override
	public GuiStyle getStyle() {
		if (style == null) return StyleManager.defaultStyle;
		return super.getStyle();
	}
	
	/**
	 * This will cause a refresh of the Gui (x|y)Size and position fields. Automatically called by {@link #setSize(Vector2i)}
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
	
	@Override
	public MLGuiClient getGui() {
		return gui;
	}

	public MLContainer getContainer() {
		return container;
	}
	
	public abstract void initControls();
	
	public abstract boolean canInteractWith(EntityPlayer epl);
}
