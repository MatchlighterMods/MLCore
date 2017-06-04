package ml.core.internal;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;

public class CommonProxy {

	public void prInit() {

	}

	public void load() {

	}

	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 */
	public EntityPlayer getPlayerEntity(FMLProxyPacket msg) {
		return ((NetHandlerPlayServer)msg.handler()).playerEntity;
	}

}
