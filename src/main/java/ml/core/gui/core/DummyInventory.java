package ml.core.gui.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class DummyInventory implements IInventory {

	protected ItemStack[] stacks;
	private int nextId = 0;
	
	public DummyInventory(int size) {
		stacks = new ItemStack[size];
	}
	
	/**
	 * DO NOT call this dynamically with variables. The server and client must be in sync.
	 * @return The next slot that hasn't been reserved in this inventory.
	 */
	public int getNextSlot() {
		return nextId++;
	}
	
	@Override
	public int getSizeInventory() {
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.stacks[i] != null) {
			ItemStack itemstack;

			if (this.stacks[i].stackSize <= j) {
				itemstack = this.stacks[i];
				this.stacks[i] = null;
				return itemstack;
			} else {
				itemstack = this.stacks[i].splitStack(j);

				if (this.stacks[i].stackSize == 0) {
					this.stacks[i] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.stacks[i] != null) {
			ItemStack itemstack = this.stacks[i];
			this.stacks[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	public void dumpItems(EntityPlayer epl) {
		for (ItemStack is : stacks) {
			if (is != null) {
				epl.dropPlayerItemWithRandomChoice(is, true);
			}
		}
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		stacks[i] = itemstack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public String getInventoryName() {
		return "Gui";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void markDirty() {}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

}
