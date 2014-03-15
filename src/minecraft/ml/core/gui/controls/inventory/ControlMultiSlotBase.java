package ml.core.gui.controls.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.item.StackUtils;
import ml.core.vec.Vector2i;

public abstract class ControlMultiSlotBase extends GuiControl implements ISlotControl {

	public List<ControlSlot> slots = new ArrayList<ControlSlot>();
	
	public ControlMultiSlotBase(GuiElement parent, Vector2i position,
			Vector2i size) {
		super(parent, position, size);
	}

	public void addSlot(Slot slot) {
		slots.add(makeControlSlot(slot));
	}
	
	/**
	 * Override this to create non-standard ControlSlots.
	 */
	protected ControlSlot makeControlSlot(Slot slot) {
		return new ControlSlot(this, slot, new Vector2i(slot.xDisplayPosition, slot.yDisplayPosition), new Vector2i(18,18));
	}
	
	public List<ControlSlot> getMergeTargets(ItemStack is) {
		return slots;
	}
	
	public boolean mergeStackInto(ItemStack is) {
		List<ControlSlot> targets = getMergeTargets(is);
		List<Slot> stargets = new ArrayList<Slot>();
		for (ControlSlot csl : targets) {
			stargets.add(csl.getSlot());
		}
		return StackUtils.mergeItemStack(is, stargets);
	}
}
