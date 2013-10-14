package ml.core.gui.controls.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;

import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class ControlSlotGrid extends GuiControl {

	public int rowLength;
	public List<ControlSlot> slots = new ArrayList<ControlSlot>();
	
	public ControlSlotGrid(GuiElement par, Vector2i pos, int perRow) {
		super(par, pos, new Vector2i());
		rowLength = perRow;
	}
	
	public ControlSlotGrid(GuiElement par, Vector2i pos, int perRow, List<Slot> slts) {
		super(par, pos, new Vector2i());
		rowLength = perRow;
		
		for (int i=0; i<slts.size(); i++) {
			addSlot(slts.get(i));
		}
	}
	
	@Override
	public void guiTick() {
		this.getSize().set(rowLength*18, (int)Math.ceil((float)slots.size()/(float)rowLength)*18);
		super.guiTick();
	}

	public void addSlot(Slot slot) {
		slots.add(new ControlSlot(this, slot, new Vector2i(slots.size()%rowLength, slots.size()/rowLength), new Vector2i(18,18)));
	}
}
