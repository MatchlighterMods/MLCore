package ml.core;

import ml.core.internal.CommonProxy;
import ml.core.internal.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Mod(modid="MLCore", name="MLCore")
@NetworkMod(clientSideRequired=false, serverSideRequired=false, channels={PacketHandler.defChan}, packetHandler=PacketHandler.class)
@TransformerExclusions({"ml"})
public class MLCore { // implements IFMLLoadingPlugin
	
	@Instance("MLCore")
	public static MLCore instance;
	
	@SidedProxy(serverSide="ml.core.internal.CommonProxy", clientSide="ml.core.internal.ClientProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.load();
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
