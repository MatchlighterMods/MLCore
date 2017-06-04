package ml.core;

import java.util.Map;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import ml.core.internal.CommonProxy;
import ml.core.internal.PacketContainerData;
import ml.core.network.PacketHandler;
import ml.core.world.WorldGenHandler;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid="MLCore", name="MLCore", version = "@VERSION@")
//@NetworkMod(clientSideRequired=false, serverSideRequired=false, channels={PacketHandler.defChan}, packetHandler=PacketHandler.class)
@TransformerExclusions({"ml"})
public class MLCore implements IFMLLoadingPlugin {
	
	public static final String mlcore_name = "MLCore";
	public PacketHandler pkh;
	
	@Instance("MLCore")
	public static MLCore instance;
	
	@SidedProxy(serverSide="ml.core.internal.CommonProxy", clientSide="ml.core.internal.ClientProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		proxy.prInit();
		
		MinecraftForge.EVENT_BUS.register(WorldGenHandler.instance);
		GameRegistry.registerWorldGenerator(WorldGenHandler.instance, 1); //TODO figure out weighting
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.load();
		
		pkh = new PacketHandler("MLCore");
		pkh.addHandler(PacketContainerData.class);
	}
	
	public String[] getASMTransformerClass() {
		return new String[]{"ml.core.asm.MLCAccessTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
