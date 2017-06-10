package ml.core.inventory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
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
	
	protected int dragMode = -1;
	protected int dragEvent = 0;
	protected final Set dragSlots = new HashSet();
	
	/** @param mode
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
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer par4EntityPlayer) {
		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = par4EntityPlayer.inventory;
		int i1;
		ItemStack itemstack3;

		if (mode == 5) {
			int l = this.dragEvent;
			this.dragEvent = Container.func_94532_c(clickedButton);

			if ((l != 1 || this.dragEvent != 2) && l != this.dragEvent) {
				this.resetDrag();
				
			} else if (inventoryplayer.getItemStack() == null) {
				this.resetDrag();
				
			} else if (this.dragEvent == 0) {
				this.dragMode = Container.func_94529_b(clickedButton);

				if (Container.func_94528_d(this.dragMode)) {
					this.dragEvent = 1;
					this.dragSlots.clear();
				} else {
					this.resetDrag();
				}
			} else if (this.dragEvent == 1) {
				Slot slot = this.inventorySlots.get(slotId);

				if (slot != null && Container.func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(slot)) {
					this.dragSlots.add(slot);
				}
			} else if (this.dragEvent == 2) {
				if (!this.dragSlots.isEmpty()) {
					itemstack3 = inventoryplayer.getItemStack().copy();
					i1 = inventoryplayer.getItemStack().stackSize;
					Iterator iterator = this.dragSlots.iterator();

					while (iterator.hasNext()) {
						Slot slot1 = (Slot)iterator.next();

						if (slot1 != null && Container.func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.dragSlots.size() && this.canDragIntoSlot(slot1)) {
							ItemStack itemstack1 = itemstack3.copy();
							int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
							Container.func_94525_a(this.dragSlots, this.dragMode, itemstack1, j1);

							if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
								itemstack1.stackSize = itemstack1.getMaxStackSize();
							}

							if (itemstack1.stackSize > slot1.getSlotStackLimit()) {
								itemstack1.stackSize = slot1.getSlotStackLimit();
							}

							i1 -= itemstack1.stackSize - j1;
							slot1.putStack(itemstack1);
						}
					}

					itemstack3.stackSize = i1;

					if (itemstack3.stackSize <= 0) {
						itemstack3 = null;
					}

					inventoryplayer.setItemStack(itemstack3);
				}

				this.resetDrag();
			} else {
				this.resetDrag();
			}
		} else if (this.dragEvent != 0) {
			this.resetDrag();
			
		} else {
			Slot slot2;
			int k1;
			ItemStack itemstack5;

			if ((mode == 0 || mode == 1) && (clickedButton == 0 || clickedButton == 1)) {
				if (slotId == -999) {
					if (inventoryplayer.getItemStack() != null && slotId == -999) {
						if (clickedButton == 0) {
							par4EntityPlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
							inventoryplayer.setItemStack((ItemStack)null);
						}

						if (clickedButton == 1) {
							par4EntityPlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);

							if (inventoryplayer.getItemStack().stackSize == 0) {
								inventoryplayer.setItemStack((ItemStack)null);
							}
						}
					}
				} else if (mode == 1) {
					if (slotId < 0) {
						return null;
					}

					slot2 = this.inventorySlots.get(slotId);

					if (slot2 != null && slot2.canTakeStack(par4EntityPlayer)) {
						itemstack3 = this.transferStackInSlot(par4EntityPlayer, slotId);

						if (itemstack3 != null) {
							Item item = itemstack3.getItem();
							itemstack = itemstack3.copy();

							if (slot2 != null && slot2.getStack() != null && slot2.getStack().getItem() == item) {
								this.retrySlotClick(slotId, clickedButton, true, par4EntityPlayer);
							}
						}
					}
				} else {
					if (slotId < 0) {
						return null;
					}

					slot2 = this.inventorySlots.get(slotId);

					if (slot2 != null) {
						itemstack3 = slot2.getStack();
						ItemStack itemstack4 = inventoryplayer.getItemStack();

						if (itemstack3 != null) {
							itemstack = itemstack3.copy();
						}

						if (itemstack3 == null) {
							if (itemstack4 != null && slot2.isItemValid(itemstack4)) {
								k1 = clickedButton == 0 ? itemstack4.stackSize : 1;

								if (k1 > slot2.getSlotStackLimit()) {
									k1 = slot2.getSlotStackLimit();
								}

								if (itemstack4.stackSize >= k1) {
									slot2.putStack(itemstack4.splitStack(k1));
								}

								if (itemstack4.stackSize == 0) {
									inventoryplayer.setItemStack((ItemStack)null);
								}
							}
						} else if (slot2.canTakeStack(par4EntityPlayer)) {
							if (itemstack4 == null) {
								k1 = clickedButton == 0 ? itemstack3.stackSize : (itemstack3.stackSize + 1) / 2;
								itemstack5 = slot2.decrStackSize(k1);
								inventoryplayer.setItemStack(itemstack5);

								if (itemstack3.stackSize == 0) {
									slot2.putStack((ItemStack)null);
								}

								slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
							} else if (slot2.isItemValid(itemstack4)) {
								if (itemstack3.getItem() == itemstack4.getItem() && itemstack3.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4)) {
									k1 = clickedButton == 0 ? itemstack4.stackSize : 1;

									if (k1 > slot2.getSlotStackLimit() - itemstack3.stackSize) {
										k1 = slot2.getSlotStackLimit() - itemstack3.stackSize;
									}

									if (k1 > itemstack4.getMaxStackSize() - itemstack3.stackSize) {
										k1 = itemstack4.getMaxStackSize() - itemstack3.stackSize;
									}

									itemstack4.splitStack(k1);

									if (itemstack4.stackSize == 0) {
										inventoryplayer.setItemStack((ItemStack)null);
									}

									itemstack3.stackSize += k1;
								} else if (itemstack4.stackSize <= slot2.getSlotStackLimit()) {
									slot2.putStack(itemstack4);
									inventoryplayer.setItemStack(itemstack3);
								}
							} else if (itemstack3.getItem() == itemstack4.getItem() && itemstack4.getMaxStackSize() > 1 && (!itemstack3.getHasSubtypes() || itemstack3.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4)) {
								k1 = itemstack3.stackSize;

								if (k1 > 0 && k1 + itemstack4.stackSize <= itemstack4.getMaxStackSize()) {
									itemstack4.stackSize += k1;
									itemstack3 = slot2.decrStackSize(k1);

									if (itemstack3.stackSize == 0) {
										slot2.putStack((ItemStack)null);
									}

									slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
								}
							}
						}

						slot2.onSlotChanged();
					}
				}
			} else if (mode == 2 && clickedButton >= 0 && clickedButton < 9) {
				slot2 = this.inventorySlots.get(slotId);

				if (slot2.canTakeStack(par4EntityPlayer)) {
					itemstack3 = inventoryplayer.getStackInSlot(clickedButton);
					boolean flag = itemstack3 == null || slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack3);
					k1 = -1;

					if (!flag) {
						k1 = inventoryplayer.getFirstEmptyStack();
						flag |= k1 > -1;
					}

					if (slot2.getHasStack() && flag) {
						itemstack5 = slot2.getStack();
						inventoryplayer.setInventorySlotContents(clickedButton, itemstack5);

						if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack3)) && itemstack3 != null) {
							if (k1 > -1) {
								inventoryplayer.addItemStackToInventory(itemstack3);
								slot2.decrStackSize(itemstack5.stackSize);
								slot2.putStack((ItemStack)null);
								slot2.onPickupFromSlot(par4EntityPlayer, itemstack5);
							}
						} else {
							slot2.decrStackSize(itemstack5.stackSize);
							slot2.putStack(itemstack3);
							slot2.onPickupFromSlot(par4EntityPlayer, itemstack5);
						}
					} else if (!slot2.getHasStack() && itemstack3 != null && slot2.isItemValid(itemstack3)) {
						inventoryplayer.setInventorySlotContents(clickedButton, (ItemStack)null);
						slot2.putStack(itemstack3);
					}
				}
			} else if (mode == 3 && par4EntityPlayer.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0) {
				slot2 = this.inventorySlots.get(slotId);

				if (slot2 != null && slot2.getHasStack()) {
					itemstack3 = slot2.getStack().copy();
					itemstack3.stackSize = itemstack3.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack3);
				}
			} else if (mode == 4 && inventoryplayer.getItemStack() == null && slotId >= 0) {
				slot2 = this.inventorySlots.get(slotId);

				if (slot2 != null && slot2.getHasStack()) {
					itemstack3 = slot2.decrStackSize(clickedButton == 0 ? 1 : slot2.getStack().stackSize);
					slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
					par4EntityPlayer.entityDropItem(itemstack3, 0);
				}
			} else if (mode == 6 && slotId >= 0) {
				slot2 = this.inventorySlots.get(slotId);
				itemstack3 = inventoryplayer.getItemStack();

				if (itemstack3 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(par4EntityPlayer))) {
					i1 = clickedButton == 0 ? 0 : this.inventorySlots.size() - 1;
					k1 = clickedButton == 0 ? 1 : -1;

					for (int l1 = 0; l1 < 2; ++l1) {
						for (int i2 = i1; i2 >= 0 && i2 < this.inventorySlots.size() && itemstack3.stackSize < itemstack3.getMaxStackSize(); i2 += k1) {
							Slot slot3 = this.inventorySlots.get(i2);

							if (slot3.getHasStack() && Container.func_94527_a(slot3, itemstack3, true) && slot3.canTakeStack(par4EntityPlayer) && this.func_94530_a(itemstack3, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize())) {
								int j2 = Math.min(itemstack3.getMaxStackSize() - itemstack3.stackSize, slot3.getStack().stackSize);
								ItemStack itemstack2 = slot3.decrStackSize(j2);
								itemstack3.stackSize += j2;

								if (itemstack2.stackSize <= 0) {
									slot3.putStack((ItemStack)null);
								}

								slot3.onPickupFromSlot(par4EntityPlayer, itemstack2);
							}
						}
					}
				}
				this.detectAndSendChanges();
			}
		}
		return itemstack;
	}
	
	protected void resetDrag() {
		this.dragEvent = 0;
		this.dragSlots.clear();
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
