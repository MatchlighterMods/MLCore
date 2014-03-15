package ml.core.gui.controls.inventory;

import java.util.List;

import ml.core.gui.MLSlot;
import ml.core.gui.core.GuiElement;
import ml.core.vec.Vector2i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

public class ControlPlayerInventory extends ControlMultiSlotBase {

	public ControlPlayerInventory(GuiElement parent, EntityPlayer epl, Vector2i position) {
		super(parent, position, new Vector2i());

		for (int slt=9; slt < epl.inventory.mainInventory.length; slt++){
			int row = (int)Math.floor(slt/9) -1;
			
			addSlot(new MLSlot(epl.inventory, slt, (slt%9)*18, row*18));
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlot(new MLSlot(epl.inventory, hotbarSlot, hotbarSlot * 18, 58));
		}
	}
	
	@Override
	public Vector2i getSize() {
		return new Vector2i(9*18, 4*18+4);
	}
	
	@Override
	public List<ControlSlot> getMergeTargets(ItemStack is) {
		return Lists.reverse(super.getMergeTargets(is));
	}
}
