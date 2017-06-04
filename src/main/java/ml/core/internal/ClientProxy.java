package ml.core.internal;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ml.core.texture.CustomTextureMapManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void load() {
		MinecraftForge.EVENT_BUS.register(CustomTextureMapManager.instance);
	}

	@Override
	public EntityPlayer getPlayerEntity(FMLProxyPacket msg) {
		if (msg.handler() instanceof NetHandlerPlayClient) return Minecraft.getMinecraft().thePlayer;
		else return super.getPlayerEntity(msg);
	}

}
