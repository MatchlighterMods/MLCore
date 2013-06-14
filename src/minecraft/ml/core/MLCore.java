package ml.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import ml.core.internal.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Mod(modid="MLCore", name="MLCore")
@NetworkMod(versionBounds="[0.5,)", clientSideRequired=false, serverSideRequired=false, channels={"MLCore"}, packetHandler=PacketHandler.class)
@TransformerExclusions({"ml"})
public class MLCore implements IFMLLoadingPlugin {
	
	@Instance("MLCore")
	public static MLCore instance;
	
	@Init
	public void init(FMLInitializationEvent evt) {
		try {
			Field f = GuiScreen.class.getField("mc");
			System.out.println(Modifier.isPublic(f.getModifiers()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
