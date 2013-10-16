package ml.core.gui.controls.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;

import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

/**
 * A control for an auto-arranged grid of slots of rowLength width
 * @author Matchlighter
 */
public class ControlSlotGrid extends GuiControl {

	private int rowLength;
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
		super.guiTick();
	}

	private Vector2i positionSlot(int idx) {
		return new Vector2i((idx%rowLength)*18, idx/rowLength*18);
	}
	
	public void addSlot(Slot slot) {
		slots.add(new ControlSlot(this, slot, positionSlot(slots.size()), new Vector2i(18,18)));
	}
	
	/**
	 * Updates the number of slots per row and calculates the new layout.
	 * @param newLength
	 */
	public void reCalculatePositions() {
		for (int s=0; s<slots.size(); s++) {
			slots.get(s).setPosition(positionSlot(s));
		}
	}
	
	public void setRowLength(int rowLength) {
		this.rowLength = rowLength;
		reCalculatePositions();
	}
	
	public int getRowLength() {
		return rowLength;
	}
	
	@Override
	public Vector2i getSize() {
		return new Vector2i(rowLength*18, (int)Math.ceil((float)slots.size()/(float)rowLength)*18);
	}
}
