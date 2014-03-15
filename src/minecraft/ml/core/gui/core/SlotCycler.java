package ml.core.gui.core;

import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;

import ml.core.gui.MLSlot;
import ml.core.gui.controls.inventory.ControlSlot;
import ml.core.gui.controls.inventory.ISlotControl;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A simple class for handling Shift+Clicking in MLGuis. You can use it to setup predefined cycles between Slots and SlotGroups 
 * @author Matchlighter
 */
public class SlotCycler {

	protected List<ISlotControl> slotsGroups = new ArrayList<ISlotControl>();
	
	public SlotCycler() {
		
	}
	
	public SlotCycler(ISlotControl... controls) {
		slotsGroups = Arrays.asList(controls);
	}
	
	private boolean cycleSlot(Slot slot, ControlSlot cslot) {
		ISlotControl group = cslot.getParent() instanceof ISlotControl ? (ISlotControl)cslot.getParent() : cslot;
		if (slotsGroups.contains(group)) {
			int index = slotsGroups.indexOf(group);
			for (int i=1; i<slotsGroups.size(); i++) { // Loop through until we find one that accepts the stack
				ISlotControl next = slotsGroups.get((index+i) % slotsGroups.size());
				ItemStack is = slot.getStack();
				if (next.mergeStackInto(is)) {
					if (is.stackSize > 0) {
						slot.onSlotChanged();
					} else {
						slot.putStack(null);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean cycleSlot(ControlSlot cslot) {
		return cycleSlot(cslot.getSlot(), cslot);
	}
	
	public boolean tryCycleSlot(Slot slot) {
		if (slot instanceof MLSlot && ((MLSlot)slot).controlSlot != null) {
			ControlSlot cslot = ((MLSlot)slot).controlSlot;
			return cycleSlot(slot, cslot);
		}
		return false;
	}
	
	public void addToCycle(ISlotControl isc) {
		if (!slotsGroups.contains(isc))
			slotsGroups.add(isc);
	}
	
	public void addToCycle(ISlotControl isc, int index) {
		if (!slotsGroups.contains(isc))
			slotsGroups.add(index, isc);
	}
	
	public void removeFromCycle(ISlotControl isc) {
		if (slotsGroups.contains(isc))
			slotsGroups.remove(isc);
	}
}
