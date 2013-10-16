package ml.core.gui.controls.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import ml.core.gui.controls.GuiControl;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;

public class ControlPlayerInventory extends GuiControl {

	public List<ControlSlot> slots = new ArrayList<ControlSlot>();
	
	public ControlPlayerInventory(GuiElement parent, EntityPlayer epl, Vector2i position) {
		super(parent, position, new Vector2i());

		for (int slt=9; slt < epl.inventory.mainInventory.length; slt++){
			int row = (int)Math.floor(slt/9) -1;
			
			addSlot(new Slot(epl.inventory, slt, 9 + (slt%9)*18, row*18));
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlot(new Slot(epl.inventory, hotbarSlot, hotbarSlot * 18, 58));
		}
	}

	private void addSlot(Slot slot) {
		slots.add(new ControlSlot(this, slot, new Vector2i(slot.xDisplayPosition, slot.yDisplayPosition), new Vector2i(18,18)));
	}
	
	@Override
	public Vector2i getSize() {
		return new Vector2i(9*18, 4*18+4);
	}
}
