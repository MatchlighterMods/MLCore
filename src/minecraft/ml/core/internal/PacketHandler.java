package ml.core.internal;

import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import ml.core.network.MLPacket;
import ml.core.network.PacketDescribeConnectable;

public class PacketHandler extends ml.core.network.PacketHandler {

	public PacketHandler() {
		addHandler(PacketDescribeConnectable.class);
	}
	
	@Override
	protected void onError(Throwable e, MLPacket mlPkt) {
		FMLLog.log(Level.SEVERE, e, "MLCore failed to process packet");
	}
}
