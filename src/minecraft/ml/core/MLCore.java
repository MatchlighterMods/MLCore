package ml.core;

import net.minecraftforge.common.MinecraftForge;
import ml.core.internal.PacketHandler;
import ml.core.texture.CustomTextureMapManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Mod(modid="MLCore", name="MLCore")
@NetworkMod(versionBounds="[0.6,)", clientSideRequired=false, serverSideRequired=false, channels={"MLCore"}, packetHandler=PacketHandler.class)
@TransformerExclusions({"ml"})
public class MLCore { // implements IFMLLoadingPlugin
	
	@Instance("MLCore")
	public static MLCore instance;
	
	@Init
	public void init(FMLInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(CustomTextureMapManager.instance);
	}
	
//	@Override
//	public String[] getLibraryRequestClass() {
//		return null;
//	}
//
//	@Override
//	public String[] getASMTransformerClass() {
//		return new String[]{"ml.core.asm.MLCAccesTransformer"};
//	}
//
//	@Override
//	public String getModContainerClass() {
//		return null;
//	}
//
//	@Override
//	public String getSetupClass() {
//		return null;
//	}
//
//	@Override
//	public void injectData(Map<String, Object> data) {
//		
//	}
}
