package ml.core.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class MLSlot extends Slot {

	public MLSlot(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean func_111238_b() {
		return false;
	}
}
