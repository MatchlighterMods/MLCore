package ml.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerUtils {

	public static boolean isRealPlayer(EntityPlayer pl){
		Package pkg = pl.getClass().getPackage();
		if (pkg == null ||!pkg.getName().contains(".") || pkg.getName().contains("net.minecraft"))
			return true;
		return false;
	}

	public static void syncClientInventory(EntityPlayer pl){
		if (pl instanceof EntityPlayerMP){
			((EntityPlayerMP)pl).sendContainerToPlayer(pl.inventoryContainer);
		}
	}
	
}
