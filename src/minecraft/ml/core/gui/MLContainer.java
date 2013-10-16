package ml.core.gui;

import java.util.ArrayList;
import java.util.List;

import ml.core.gui.core.TopParentGuiElement;
import ml.core.internal.PacketContainerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

public class MLContainer extends Container {
	
	protected List<Slot> slots = new ArrayList<Slot>();
	protected TopParentGuiElement priElemement;
	
	public MLContainer(TopParentGuiElement elm) {
		priElemement = elm;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return priElemement.canInteractWith(entityplayer);
	}
	
	/**
	 * For data transmission when 2 shorts isn't enough
	 */
	public void handleDataPacket(NBTTagCompound pload, Side side) {
		// TODO Relay to priElement
	}
	
	public void sendPacket(NBTTagCompound payload) {
		Packet250CustomPayload pkt = new PacketContainerData(windowId, payload).convertToPkt250();
		// TODO Send packet to using players
	}

	@Override
	public void putStackInSlot(int par1, ItemStack par2ItemStack) {
		// TODO Put stack in the slot ?
		// Called by network code. Override may not be necessary w/ getSlot() overridden
		super.putStackInSlot(par1, par2ItemStack);
	}
	
	@Override
	public Slot getSlot(int par1) {
		return slots.get(par1);
	}
	
	/**
	 * DO NOT call dynamically. Call once per slot on init. Not again. Must be synced between Client and server. <br/>
	 * <b>Note:</b> ControlSlot automatically handles this on instantiation.
	 */
	public int registerSlot(Slot slt) {
		if (!slots.contains(slt))
			slots.add(slt);
		slt.slotNumber = slots.indexOf(slt);
		return slt.slotNumber;
	}
}
