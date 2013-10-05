package ml.core.internal;

import ml.core.texture.CustomTextureMapManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void load() {
		MinecraftForge.EVENT_BUS.register(CustomTextureMapManager.instance);
	}

}
