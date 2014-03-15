package ml.core.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

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
	 * Attempts to merge the given {@link ItemStack} into one or multiple of the given {@link Slot}s
	 * @return There was partial success
	 */
	public static boolean mergeItemStack(ItemStack is, List<Slot> targets) {
		boolean didSomething = false;

		ItemStack stackOn;

		if (is.isStackable()) {
			for (Slot slot : targets) {
				stackOn = slot.getStack();
				
				if (stackOn != null && stackOn.getItem() == is.getItem() && (!is.getHasSubtypes() || is.getItemDamage() == stackOn.getItemDamage()) && ItemStack.areItemStackTagsEqual(is, stackOn)) {
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
				if (is.stackSize <= 0) break;
			}
		}

		if (is.stackSize > 0) {
			for (Slot slot : targets) {
				stackOn = slot.getStack();
				if (stackOn == null && slot.isItemValid(is)) {
					int delta = Math.min(is.stackSize, slot.getSlotStackLimit());
					ItemStack cpy = is.copy();
					cpy.stackSize = delta;
					
					slot.putStack(cpy);
					is.stackSize -= delta;
					didSomething = true;
				}
				if (is.stackSize <= 0) break;
			}
		}

		return didSomething;
	}
	
	/**
	 * mergeItemStack function that respects getSlotStackLimit() and isItemValid()
	 */
	public static boolean mergeItemStack(ItemStack is, int lbound, int ubound, boolean reverse, Container cont) {
		
		List<Slot> targets = new ArrayList<Slot>();
		for (int i=lbound; i<ubound; i++) {
			targets.add(cont.getSlot(i));
		}
		if (reverse) {
			targets = Lists.reverse(targets);
		}
		
		return mergeItemStack(is, targets);
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
