package ml.core.gui.controls.inventory;

import java.util.List;

import ml.core.gui.MLSlot;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * A control for an auto-arranged grid of slots of rowLength width
 * @author Matchlighter
 */
public class ControlSlotGrid extends ControlMultiSlotBase {

	private int rowLength;
	
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
	
	public ControlSlotGrid(GuiElement par, Vector2i pos, int perRow, IInventory inv) {
		super(par, pos, new Vector2i());
		rowLength = perRow;
		
		for (int i=0; i<inv.getSizeInventory(); i++) {
			addSlot(new MLSlot(inv, i, 0, 0));
		}
	}
	
	
	@Override
	public void guiTick() {
		super.guiTick();
	}

	protected Vector2i positionSlot(int idx) {
		return new Vector2i((idx%rowLength)*18, idx/rowLength*18);
	}
	
	@Override
	protected ControlSlot makeControlSlot(Slot slot) {
		return new ControlSlot(this, slot, positionSlot(slots.size()), new Vector2i(18,18));
	}
	
	/**
	 * Updates the number of slots per row and calculates the new layout.
	 * @param newLength
	 */
	public void reCalculatePositions() {
		for (int s=0; s<slots.size(); s++) {
			slots.get(s).setLocalPosition(positionSlot(s));
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
