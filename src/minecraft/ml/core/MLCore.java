package ml.core;

import java.util.Map;

import net.minecraft.client.Minecraft;

import ml.core.internal.PacketHandler;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Mod(modid="MLCore", name="MLCore")
@NetworkMod(versionBounds="[0.5,)", clientSideRequired=false, serverSideRequired=false, channels={"MLCore"}, packetHandler=PacketHandler.class)
@TransformerExclusions({"ml"})
public class MLCore implements IFMLLoadingPlugin {
	
	@Instance("MLCore")
	public static MLCore instance;
	
	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"ml.core.asm.MLCAccesTransformer"};
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
}
