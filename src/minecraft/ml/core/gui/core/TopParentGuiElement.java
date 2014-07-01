package ml.core.gui.core;

import ml.core.gui.core.style.GuiStyle;
import ml.core.gui.event.EventGuiClosing;
import ml.core.gui.event.GuiEvent;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * TopParentGuiObjects house all other GuiObjects in a Gui. <br/>
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
	public Vector2i gmousePos;
	
	public GuiElement hoverElement;
	public GuiElement focusedElement;
	
	/**
	 * This inventory belongs to the gui. Any items will be dropped when it is closed. By default it can store 64 stacks.
	 */
	public DummyInventory guiInventory = new DummyInventory(64);

	public TopParentGuiElement(EntityPlayer epl, Side side) {
		super(null);
		this.side = side;
		this.player = epl;
		this.container = new MLContainer(this);
		if (side == Side.CLIENT) {
			this.gui = new MLGuiClient(this);
			this.style = GuiStyle.defaultStyle;
			this.gmousePos = new Vector2i();
		}
	}
	
	@Override
	public Vector2i getLocalPosition() {
		if (gui == null) return super.getLocalPosition();
		return gui.getPosition();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiStyle getStyle() {
		if (style == null) return GuiStyle.defaultStyle;
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
	
	public void sendPacket(NBTTagCompound payload, Side sendTo) {
		this.container.sendPacket(payload, sendTo);
	}
	
	public void sendPacket(NBTTagCompound payload) {
		if (side == Side.CLIENT) {
			sendPacket(payload, Side.SERVER);
		} else {
			sendPacket(payload, Side.CLIENT);
		}
	}
	
	@Override
	public void handleEvent(GuiEvent evt) {
		if (evt instanceof EventGuiClosing && side == Side.SERVER) {
			guiInventory.dumpItems(player);
		}
		
		super.handleEvent(evt);
	}
	
	public boolean shouldShowNEI() {
		return true;
	}
	
	public abstract ItemStack transferStackFromSlot(EntityPlayer epl, Slot slot);
	
	public abstract void initControls();
	
	public abstract boolean canInteractWith(EntityPlayer epl);
}
