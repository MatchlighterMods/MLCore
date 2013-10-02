package ml.core.item;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtils {

	/**
	 * Drops an {@link EntityItem} of the specified {@link ItemStack} in the specified {@link World} at the specified coordinates.
	 */
	public static void dropItemIntoWorld(World par1World, double x, double y, double z, double velx, double vely, double velz, ItemStack par5ItemStack) {
		if (!par1World.isRemote) {
			EntityItem entityitem = new EntityItem(par1World, x, y, z, par5ItemStack);
			entityitem.motionX = velx;
			entityitem.motionY = vely;
			entityitem.motionZ = velz;
			entityitem.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(entityitem);
		}
	}
	
	/**
	 * Drops an {@link EntityItem} of the specified {@link ItemStack} in the specified {@link World} at the specified coordinates.
	 */
	public static void dropItemIntoWorld(World par1World, double x, double y, double z, ItemStack par5ItemStack) {
		if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			EntityItem entityitem = new EntityItem(par1World, x, y, z, par5ItemStack);
			entityitem.delayBeforeCanPickup = 10;
			par1World.spawnEntityInWorld(entityitem);
		}
	}

	/**
	 * Mimics Minecraft's block dropping with a random variation area. Minecraft uses a 0.7 variation.
	 */
	public static void dropItemIntoWorld(World par1World, float x, float y, float z, ItemStack par5ItemStack, float variationArea) {
		double d0 = (double)(par1World.rand.nextFloat() * variationArea) + (double)(1.0F - variationArea) * 0.5D;
		double d1 = (double)(par1World.rand.nextFloat() * variationArea) + (double)(1.0F - variationArea) * 0.5D;
		double d2 = (double)(par1World.rand.nextFloat() * variationArea) + (double)(1.0F - variationArea) * 0.5D;
		dropItemIntoWorld(par1World, x + d0, y + d1, z + d2, par5ItemStack);
	}
	
	/**
	 * End-all function of checking if two items equal.
	 * @param target Can be an {@link ArrayList}&lt;ItemStack&gt;, {@link ItemStack}, or an {@link Item}
	 */
	public static boolean checkItemEquals(Object target, ItemStack input) {
		if (input == null && target != null || input != null && target == null) {
			return false;
		}
		
		if (target instanceof ArrayList) {
			for (ItemStack is : (ArrayList<ItemStack>)target) {
				if (checkItemEquals(input, is))
					return true;
			}
		} else if (target instanceof ItemStack) {
			ItemStack trgIS = (ItemStack)target;
			return (trgIS.itemID == input.itemID && (trgIS.getItemDamage() == OreDictionary.WILDCARD_VALUE|| trgIS.getItemDamage() == input.getItemDamage()));
		} else if (target instanceof Item) {
			return (((Item)target).itemID == input.itemID);
		}
		return false;
	}
	
	/**
	 * Takes an ItemStack of OreDictionary dye and returns the vanilla Dye Id for that color
	 */
	public static int getVanillaColorId(ItemStack mOre) {
		for (int i=0; i<16; i++){
			if (OreDictionary.getOreID(new ItemStack(Item.dyePowder, 1, i)) == OreDictionary.getOreID(mOre)){
				return i;
			}
		}
		return 0;
	}
}
