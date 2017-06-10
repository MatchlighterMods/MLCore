package ml.core.item;

import java.util.ArrayList;
import java.util.List;

import ml.core.data.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;

public class StackUtils {

	public static ItemStack create(Item item, int count, int meta, NBTTagCompound nbt) {
		ItemStack stack = new ItemStack(item, count, meta);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static ItemStack create(Block block, int count, int meta, NBTTagCompound nbt) {
		ItemStack stack = new ItemStack(block, count, meta);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	/**
	 * Drops an {@link EntityItem} of the specified {@link ItemStack} in the specified {@link World} at the specified coordinates.
	 */
	public static void dropStackIntoWorld(World par1World, double x, double y, double z, double velx, double vely, double velz, ItemStack par5ItemStack) {
		if (!par1World.isRemote) {
			EntityItem entityitem = new EntityItem(par1World, x, y, z, par5ItemStack);
			entityitem.motionX = velx;
			entityitem.motionY = vely;
			entityitem.motionZ = velz;
			entityitem.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(entityitem);
		}
	}
	
	/**
	 * Drops an {@link EntityItem} of the specified {@link ItemStack} in the specified {@link World} at the specified coordinates.
	 */
	public static void dropStackIntoWorld(World par1World, double x, double y, double z, ItemStack par5ItemStack) {
		if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			EntityItem entityitem = new EntityItem(par1World, x, y, z, par5ItemStack);
			entityitem.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(entityitem);
		}
	}

	/**
	 * Mimics Minecraft's block dropping with a random variation area. Minecraft uses a 0.7 variation.
	 */
	public static void dropStackIntoWorld(World par1World, float x, float y, float z, ItemStack par5ItemStack, float variationArea) {
		double d0 = (double)(par1World.rand.nextFloat() * variationArea) + (double)(1.0F - variationArea) * 0.5D;
		double d1 = (double)(par1World.rand.nextFloat() * variationArea) + (double)(1.0F - variationArea) * 0.5D;
		double d2 = (double)(par1World.rand.nextFloat() * variationArea) + (double)(1.0F - variationArea) * 0.5D;
		dropStackIntoWorld(par1World, x + d0, y + d1, z + d2, par5ItemStack);
	}
	
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
	 * Attempts to merge the given {@link ItemStack} into one or multiple of the given {@link Slot}s. Respects getSlotStackLimit() and isItemValid().
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
	 * End-all function of checking if two items equal.
	 * @param target Can be an {@link ArrayList}&lt;ItemStack&gt;, {@link ItemStack}, or an {@link Item}
	 */
	public static boolean checkItemEquals(Object target, ItemStack input) {
		if (input == null && target != null || input != null && target == null) {
			return false;
		}
		
		if (target instanceof ArrayList) {
			for (ItemStack is : (ArrayList<ItemStack>)target) {
				if (checkItemEquals(input, is))
					return true;
			}
		} else if (target instanceof ItemStack) {
			ItemStack trgIS = (ItemStack)target;
			return (trgIS.getItem() == input.getItem() && (trgIS.getItemDamage() == OreDictionary.WILDCARD_VALUE || trgIS.getItemDamage() == input.getItemDamage()));
		} else if (target instanceof Item) {
			return (((Item)target) == input.getItem());
		}
		return false;
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
