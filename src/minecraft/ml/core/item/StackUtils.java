package ml.core.item;

import ml.core.data.NBTUtils;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StackUtils {

	public static NBTTagCompound getStackTag(ItemStack is) {
		if (!is.hasTagCompound()){
			is.setTagCompound(new NBTTagCompound());
		}
		return is.getTagCompound();
	}
	
	public static <T> T getTag(ItemStack is, T defaultVal, String...tagPath) {
		if (!is.hasTagCompound()) return defaultVal;
		NBTTagCompound tag = is.getTagCompound();
		return NBTUtils.getTagValue(tag, defaultVal, tagPath);
	}
	
	public static <T> void setTag(ItemStack is, T value, String... tagPath) {
		NBTUtils.setTag(getStackTag(is), value, tagPath);
	}
	
	public static boolean hasTagAt(ItemStack is, String...tagPath) {
		if (!is.hasTagCompound()) return false;
		return NBTUtils.hasTagAt(is.getTagCompound(), tagPath);
	}
	
	/**
	 * mergeItemStack function that respects getSlotStackLimit() and isItemValid()
	 */
	public static boolean mergeItemStack(ItemStack is, int lbound, int ubound, boolean reverse, Container cont) {
		boolean didSomething = false;
		int itI = lbound;

		if (reverse) {
			itI = ubound - 1;
		}

		Slot slot;
		ItemStack stackOn;

		if (is.isStackable()) {
			while (is.stackSize > 0 && (!reverse && itI < ubound || reverse && itI >= lbound)) {
				slot = (Slot)cont.inventorySlots.get(itI);
				stackOn = slot.getStack();

				if (stackOn != null && stackOn.itemID == is.itemID && (!is.getHasSubtypes() || is.getItemDamage() == stackOn.getItemDamage()) && ItemStack.areItemStackTagsEqual(is, stackOn)) {
					int var9 = stackOn.stackSize + is.stackSize;

					int mSize = Math.min(is.getMaxStackSize(), slot.getSlotStackLimit());
					if (var9 <= mSize) {
						is.stackSize = 0;
						stackOn.stackSize = var9;
						slot.onSlotChanged();
						didSomething = true;
					} else if (stackOn.stackSize < mSize) {
						is.stackSize -= mSize - stackOn.stackSize;
						stackOn.stackSize = mSize;
						slot.onSlotChanged();
						didSomething = true;
					}
				}

				if (reverse) {
					--itI;
				} else {
					++itI;
				}
			}
		}

		if (is.stackSize > 0) {
			if (reverse) {
				itI = ubound - 1;
			} else {
				itI = lbound;
			}

			while (is.stackSize > 0 && (!reverse && itI < ubound || reverse && itI >= lbound)) {
				slot = (Slot)cont.inventorySlots.get(itI);
				stackOn = slot.getStack();

				if (stackOn == null && slot.isItemValid(is)) {
					int delta = Math.min(is.stackSize, slot.getSlotStackLimit());
					ItemStack cpy = is.copy();
					cpy.stackSize = delta;
					
					slot.putStack(cpy);
					is.stackSize -= delta;
					didSomething = true;
				}

				if (reverse) {
					--itI;
				} else {
					++itI;
				}
			}
		}

		return didSomething;
	}
	
	/**
	 * A simple subclass of {@link Slot} that checks with it's inventory if an ItemStack is valid.
	 * @author Matchlighter
	 */
	public static class RSlot extends Slot {

		public RSlot(IInventory par1iInventory, int par2, int par3, int par4) {
			super(par1iInventory, par2, par3, par4);
		}
		
		@Override
		public boolean isItemValid(ItemStack par1ItemStack) {
			return inventory.isItemValidForSlot(this.getSlotIndex(), par1ItemStack) && super.isItemValid(par1ItemStack);
		}
	}
}
