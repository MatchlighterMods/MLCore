package ml.core.gui;

import ml.core.gui.controls.inventory.ControlSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class MLSlot extends Slot {

	/**
	 * There are some cases where this may be inaccurate, since multiple ControlSlots can have the same slot.
	 */
	public ControlSlot controlSlot;
	
	public MLSlot(IInventory par1iInventory, int par2, int par3, int par4, ControlSlot owner) {
		super(par1iInventory, par2, par3, par4);
		controlSlot = owner;
	}
	
	public MLSlot(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	// Disable MC hover effect; leave that to the ControlSlot
	@Override
	public boolean canBeHovered() {
		return false;
	}
}
