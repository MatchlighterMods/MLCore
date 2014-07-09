package ml.core.inventory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * I frequently find myself having to rewrite {@link Container#slotClick(int, int, int, EntityPlayer)}
 * just to point it to a custom implementation of a slot list. So I decided to make a breakout class, intended for use as an anonymous inner class
 * @author Matchlighter
 */
public abstract class CustomSlotClick {

	public List<Slot> inventorySlots;

	public CustomSlotClick() {
		
	}
	
	public CustomSlotClick(List<Slot> slts) {
		inventorySlots = slts;
	}
	
	protected int field_94535_f = -1;
	protected int field_94536_g = 0;
	protected final Set field_94537_h = new HashSet();
	
	/** @param action
	 * <ul>
	 * <li>0 - Standard action. Arg: mouseButton</li>
	 * <li>1 - Slot merge into my inventory</li>
	 * <li>2 - Move to Hotbar. Arg: targetSlot</li>
	 * <li>3 - Creative pick stack</li>
	 * <li>4 - Drop Item. Arg: 0 Drop One, 1 Drop All</li>
	 * <li>5 - Stack drag over</li>
	 * <li>6 - Merge into held stack. Arg: IteratorDir 0+, 1-</li>
	 * </ul>
	 */
	public ItemStack slotClick(int slotNum, int arg, int action, EntityPlayer par4EntityPlayer) {
		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = par4EntityPlayer.inventory;
		int l;
		ItemStack itemstack1;

		if (action == 5) {
			int i1 = this.field_94536_g;
			this.field_94536_g = Container.func_94532_c(arg);

			if ((i1 != 1 || this.field_94536_g != 2) && i1 != this.field_94536_g) {
				this.func_94533_d();
			} else if (inventoryplayer.getItemStack() == null) {
				this.func_94533_d();
			} else if (this.field_94536_g == 0) {
				this.field_94535_f = Container.func_94529_b(arg);

				if (Container.func_94528_d(this.field_94535_f)) {
					this.field_94536_g = 1;
					this.field_94537_h.clear();
				} else {
					this.func_94533_d();
				}
			} else if (this.field_94536_g == 1) {
				Slot slot = this.inventorySlots.get(slotNum);

				if (slot != null && Container.func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.field_94537_h.size() && this.canDragIntoSlot(slot)) {
					this.field_94537_h.add(slot);
				}
			} else if (this.field_94536_g == 2) {
				if (!this.field_94537_h.isEmpty()) {
					itemstack1 = inventoryplayer.getItemStack().copy();
					l = inventoryplayer.getItemStack().stackSize;
					Iterator iterator = this.field_94537_h.iterator();

					while (iterator.hasNext()) {
						Slot slot1 = (Slot)iterator.next();

						if (slot1 != null && Container.func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.field_94537_h.size() && this.canDragIntoSlot(slot1)) {
							ItemStack itemstack2 = itemstack1.copy();
							int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
							Container.func_94525_a(this.field_94537_h, this.field_94535_f, itemstack2, j1);

							if (itemstack2.stackSize > itemstack2.getMaxStackSize()) {
								itemstack2.stackSize = itemstack2.getMaxStackSize();
							}

							if (itemstack2.stackSize > slot1.getSlotStackLimit()) {
								itemstack2.stackSize = slot1.getSlotStackLimit();
							}

							l -= itemstack2.stackSize - j1;
							slot1.putStack(itemstack2);
						}
					}

					itemstack1.stackSize = l;

					if (itemstack1.stackSize <= 0) {
						itemstack1 = null;
					}

					inventoryplayer.setItemStack(itemstack1);
				}

				this.func_94533_d();
			} else {
				this.func_94533_d();
			}
		} else if (this.field_94536_g != 0) {
			this.func_94533_d();
		} else {
			Slot slot2;
			int k1;
			ItemStack itemstack3;

			if ((action == 0 || action == 1) && (arg == 0 || arg == 1)) {
				if (slotNum == -999) {
					if (inventoryplayer.getItemStack() != null && slotNum == -999) {
						if (arg == 0) {
							par4EntityPlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
							inventoryplayer.setItemStack((ItemStack)null);
						}

						if (arg == 1) {
							par4EntityPlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);

							if (inventoryplayer.getItemStack().stackSize == 0) {
								inventoryplayer.setItemStack((ItemStack)null);
							}
						}
					}
				} else if (action == 1) {
					if (slotNum < 0) {
						return null;
					}

					slot2 = this.inventorySlots.get(slotNum);

					if (slot2 != null && slot2.canTakeStack(par4EntityPlayer)) {
						itemstack1 = this.transferStackInSlot(par4EntityPlayer, slotNum);

						if (itemstack1 != null) {
							l = itemstack1.itemID;
							itemstack = itemstack1.copy();

							if (slot2 != null && slot2.getStack() != null && slot2.getStack().itemID == l) {
								this.retrySlotClick(slotNum, arg, true, par4EntityPlayer);
							}
						}
					}
				} else {
					if (slotNum < 0) {
						return null;
					}

					slot2 = this.inventorySlots.get(slotNum);

					if (slot2 != null) {
						itemstack1 = slot2.getStack();
						ItemStack itemstack4 = inventoryplayer.getItemStack();

						if (itemstack1 != null) {
							itemstack = itemstack1.copy();
						}

						if (itemstack1 == null) {
							if (itemstack4 != null && slot2.isItemValid(itemstack4)) {
								k1 = arg == 0 ? itemstack4.stackSize : 1;

								if (k1 > slot2.getSlotStackLimit()) {
									k1 = slot2.getSlotStackLimit();
								}

								slot2.putStack(itemstack4.splitStack(k1));

								if (itemstack4.stackSize == 0) {
									inventoryplayer.setItemStack((ItemStack)null);
								}
							}
						} else if (slot2.canTakeStack(par4EntityPlayer)) {
							if (itemstack4 == null) {
								k1 = arg == 0 ? itemstack1.stackSize : (itemstack1.stackSize + 1) / 2;
								itemstack3 = slot2.decrStackSize(k1);
								inventoryplayer.setItemStack(itemstack3);

								if (itemstack1.stackSize == 0) {
									slot2.putStack((ItemStack)null);
								}

								slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
							} else if (slot2.isItemValid(itemstack4)) {
								if (itemstack1.itemID == itemstack4.itemID && itemstack1.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4)) {
									k1 = arg == 0 ? itemstack4.stackSize : 1;

									if (k1 > slot2.getSlotStackLimit() - itemstack1.stackSize) {
										k1 = slot2.getSlotStackLimit() - itemstack1.stackSize;
									}

									if (k1 > itemstack4.getMaxStackSize() - itemstack1.stackSize) {
										k1 = itemstack4.getMaxStackSize() - itemstack1.stackSize;
									}

									itemstack4.splitStack(k1);

									if (itemstack4.stackSize == 0) {
										inventoryplayer.setItemStack((ItemStack)null);
									}

									itemstack1.stackSize += k1;
								} else if (itemstack4.stackSize <= slot2.getSlotStackLimit()) {
									slot2.putStack(itemstack4);
									inventoryplayer.setItemStack(itemstack1);
								}
							} else if (itemstack1.itemID == itemstack4.itemID && itemstack4.getMaxStackSize() > 1 && (!itemstack1.getHasSubtypes() || itemstack1.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4)) {
								k1 = itemstack1.stackSize;

								if (k1 > 0 && k1 + itemstack4.stackSize <= itemstack4.getMaxStackSize()) {
									itemstack4.stackSize += k1;
									itemstack1 = slot2.decrStackSize(k1);

									if (itemstack1.stackSize == 0) {
										slot2.putStack((ItemStack)null);
									}

									slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
								}
							}
						}

						slot2.onSlotChanged();
					}
				}
			} else if (action == 2 && arg >= 0 && arg < 9) {
				slot2 = this.inventorySlots.get(slotNum);

				if (slot2.canTakeStack(par4EntityPlayer)) {
					itemstack1 = inventoryplayer.getStackInSlot(arg);
					boolean flag = itemstack1 == null || slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack1);
					k1 = -1;

					if (!flag) {
						k1 = inventoryplayer.getFirstEmptyStack();
						flag |= k1 > -1;
					}

					if (slot2.getHasStack() && flag) {
						itemstack3 = slot2.getStack();
						inventoryplayer.setInventorySlotContents(arg, itemstack3);

						if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack1)) && itemstack1 != null) {
							if (k1 > -1) {
								inventoryplayer.addItemStackToInventory(itemstack1);
								slot2.decrStackSize(itemstack3.stackSize);
								slot2.putStack((ItemStack)null);
								slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
							}
						} else {
							slot2.decrStackSize(itemstack3.stackSize);
							slot2.putStack(itemstack1);
							slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
						}
					} else if (!slot2.getHasStack() && itemstack1 != null && slot2.isItemValid(itemstack1)) {
						inventoryplayer.setInventorySlotContents(arg, (ItemStack)null);
						slot2.putStack(itemstack1);
					}
				}
			} else if (action == 3 && par4EntityPlayer.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotNum >= 0) {
				slot2 = this.inventorySlots.get(slotNum);

				if (slot2 != null && slot2.getHasStack()) {
					itemstack1 = slot2.getStack().copy();
					itemstack1.stackSize = itemstack1.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack1);
				}
			} else if (action == 4 && inventoryplayer.getItemStack() == null && slotNum >= 0) {
				slot2 = this.inventorySlots.get(slotNum);

				if (slot2 != null && slot2.getHasStack()) {
					itemstack1 = slot2.decrStackSize(arg == 0 ? 1 : slot2.getStack().stackSize);
					slot2.onPickupFromSlot(par4EntityPlayer, itemstack1);
					par4EntityPlayer.dropPlayerItemWithRandomChoice(itemstack1, true);
				}
			} else if (action == 6 && slotNum >= 0) {
				slot2 = this.inventorySlots.get(slotNum);
				itemstack1 = inventoryplayer.getItemStack();

				if (itemstack1 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(par4EntityPlayer))) {
					l = arg == 0 ? 0 : this.inventorySlots.size() - 1;
					k1 = arg == 0 ? 1 : -1;

					for (int l1 = 0; l1 < 2; ++l1) {
						for (int i2 = l; i2 >= 0 && i2 < this.inventorySlots.size() && itemstack1.stackSize < itemstack1.getMaxStackSize(); i2 += k1) {
							Slot slot3 = this.inventorySlots.get(i2);

							if (slot3.getHasStack() && Container.func_94527_a(slot3, itemstack1, true) && slot3.canTakeStack(par4EntityPlayer) && this.func_94530_a(itemstack1, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize())) {
								int j2 = Math.min(itemstack1.getMaxStackSize() - itemstack1.stackSize, slot3.getStack().stackSize);
								ItemStack itemstack5 = slot3.decrStackSize(j2);
								itemstack1.stackSize += j2;

								if (itemstack5.stackSize <= 0) {
									slot3.putStack((ItemStack)null);
								}

								slot3.onPickupFromSlot(par4EntityPlayer, itemstack5);
							}
						}
					}
				}
				this.detectAndSendChanges();
			}
		}
		return itemstack;
	}
	protected void func_94533_d() {
		this.field_94536_g = 0;
		this.field_94537_h.clear();
	}

	public boolean canDragIntoSlot(Slot par1Slot) {
		return true;
	}

	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
		return true;
	}

	public void detectAndSendChanges() {}

	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
		this.slotClick(par1, par2, 1, par4EntityPlayer);
	}

	public abstract ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2);
}
