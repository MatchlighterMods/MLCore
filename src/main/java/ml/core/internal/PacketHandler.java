package ml.core.internal;


public final class PacketHandler extends ml.core.network.PacketHandler {

	public static final String defChan = "MLCore";
	
	public PacketHandler() {
		addHandler(PacketContainerData.class);
	}
	
}
