package ml.core.lib;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerLib {

	public static boolean isRealPlayer(EntityPlayer pl){
		Package pkg = pl.getClass().getPackage();
		if (pkg == null ||!pkg.getName().contains(".") || pkg.getName().contains("net.minecraft"))
			return true;
		return false;
	}

}
