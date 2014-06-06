package ml.core.gui.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ml.core.gui.MLSlot;
import ml.core.gui.controls.inventory.ControlSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A simple class for handling Shift+Clicking in MLGuis. You can use it to setup predefined cycles between Slots and SlotGroups 
 * @author Matchlighter
 */
public class SlotCycler {

	protected List<IStackMergeTarget> slotsGroups = new ArrayList<IStackMergeTarget>();
	
	public SlotCycler() {
		
	}
	
	public SlotCycler(IStackMergeTarget... controls) {
		slotsGroups = new ArrayList<SlotCycler.IStackMergeTarget>(Arrays.asList(controls));
	}
	
	private boolean cycleSlot(Slot slot, ControlSlot cslot) {
		if (slot.getHasStack()) {
			IStackMergeTarget group = cslot;
			// Find the highest MergeTarget in this cycler
			for (GuiElement el : cslot.getAncestors()) {
				if (el instanceof IStackMergeTarget && slotsGroups.contains(el)) {
					group = (IStackMergeTarget)el;
				}
			}
			
			if (slotsGroups.contains(group)) {
				int index = slotsGroups.indexOf(group);
				for (int i=1; i<slotsGroups.size(); i++) { // Loop through until we find one that accepts the stack
					IStackMergeTarget next = slotsGroups.get((index+i) % slotsGroups.size());
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
		}
		return false;
	}
	
	public boolean cycleSlot(ControlSlot cslot) {
		return cycleSlot(cslot.getSlot(), cslot);
	}
	
	public boolean cycleSlot(Slot slot) {
		if (slot instanceof MLSlot && ((MLSlot)slot).controlSlot != null) {
			ControlSlot cslot = ((MLSlot)slot).controlSlot;
			return cycleSlot(slot, cslot);
		}
		return false;
	}
	
	public void addToCycle(IStackMergeTarget isc) {
		if (!slotsGroups.contains(isc))
			slotsGroups.add(isc);
	}
	
	public void addToCycle(IStackMergeTarget isc, int index) {
		if (!slotsGroups.contains(isc))
			slotsGroups.add(index, isc);
	}
	
	public void removeFromCycle(IStackMergeTarget isc) {
		if (slotsGroups.contains(isc))
			slotsGroups.remove(isc);
	}
	
	public static interface IStackMergeTarget {
		
		public boolean mergeStackInto(ItemStack is);
		
	}

}
