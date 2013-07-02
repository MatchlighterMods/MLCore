package ml.core.internal;

import ml.core.network.PacketDescribeConnectable;

public class PacketHandler extends ml.core.network.PacketHandler {

	public PacketHandler() {
		addHandler(PacketDescribeConnectable.class);
	}
	
}
