package ml.core.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtils {

	public static boolean containsItem(IInventory inv, Item it) {
		return findItemSlot(inv, it) > -1;
	}
	
	public static ItemStack findItem(IInventory inv, Item it) {
		int slotNum = findItemSlot(inv, it);
		if (slotNum>-1)
			return inv.getStackInSlot(slotNum);
		return null;
	}
	
	public static int findItemSlot(IInventory inv, Item it) {
		for (int i=0; i<inv.getSizeInventory(); i++) {
			ItemStack is = inv.getStackInSlot(i);
			if (is != null && is.getItem() == it) return i;
		}
		return -1;
	}
	
	public static int countItems(IInventory inv, Item it) {
		int cnt = 0;
		for (ItemStack is : findItems(inv, it)) {
			cnt += is.stackSize;
		}
		return cnt;
	}
	
	public static List<ItemStack> findItems(IInventory inv, Item it) {
		List<Integer> lst = findItemSlots(inv, it);
		List<ItemStack> islst = new ArrayList<ItemStack>();
		for (int i : lst) {
			islst.add(inv.getStackInSlot(i));
		}
		return islst;
	}
	
	public static List<Integer> findItemSlots(IInventory inv, Item it) {
		List<Integer> lst = new ArrayList<Integer>();
		for (int i=0; i<inv.getSizeInventory(); i++) {
			ItemStack is = inv.getStackInSlot(i);
			if (is != null && is.getItem() == it) lst.add(i);
		}
		return lst;
	}
	
	public static int countStacksOfItem(IInventory inv, Item it) {
		return findItemSlots(inv, it).size();
	}
}
