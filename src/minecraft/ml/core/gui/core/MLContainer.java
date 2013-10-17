package ml.core.gui.core;

import java.util.ArrayList;
import java.util.List;

import ml.core.internal.PacketContainerData;
import ml.core.inventory.CustomSlotClick;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
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

	@Override
	public void detectAndSendChanges() {
		for (int i = 0; i < slots.size(); ++i) {
			ItemStack itemstack = ((Slot)this.slots.get(i)).getStack();
			ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);

			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
				itemstack1 = itemstack == null ? null : itemstack.copy();
				this.inventoryItemStacks.set(i, itemstack1);

				for (int j = 0; j < this.crafters.size(); ++j) {
					((ICrafting)this.crafters.get(j)).sendSlotContents(this, i, itemstack1);
				}
			}
		}
	}

	CustomSlotClick csc = new CustomSlotClick(slots) {
		public void detectAndSendChanges() {
			MLContainer.this.detectAndSendChanges();
		};
		
		@Override
		public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3,
			EntityPlayer par4EntityPlayer) {
		return csc.slotClick(par1, par2, par3, par4EntityPlayer);
	}

	/**
	 * DO NOT call dynamically. Call once per slot on init. Not again. Must be synced between Client and server. <br/>
	 * <b>Note:</b> ControlSlot automatically handles this on instantiation.
	 */
	@Override
	public Slot addSlotToContainer(Slot slt) {
		if (!slots.contains(slt))
			slots.add(slt);
		slt.slotNumber = slots.indexOf(slt);
		this.inventoryItemStacks.add((Object)null);
		return slt;
	}
	
	@Override
	public List getInventory() {
		ArrayList arraylist = new ArrayList();

		for (int i = 0; i < this.slots.size(); ++i) {
			arraylist.add(((Slot)this.slots.get(i)).getStack());
		}

		return arraylist;
	}
}
