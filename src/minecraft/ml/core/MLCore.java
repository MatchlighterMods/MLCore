package ml.core;

import ml.core.internal.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="MLCore", name="MLCore")
@NetworkMod(versionBounds="[0.5,)", clientSideRequired=false, serverSideRequired=false, channels={"MLCore"}, packetHandler=PacketHandler.class)
public class MLCore {
	
	@Instance("MLCore")
	public static MLCore instance;
}
