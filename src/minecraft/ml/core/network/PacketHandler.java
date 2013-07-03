package ml.core.network;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * Thanks to MachineMuse for the idea on how to implement this
 * @author Matchlighter
 */
public abstract class PacketHandler implements IPacketHandler {
		
	protected static BiMap<Integer, Class<? extends MLPacket>> PacketTypes = HashBiMap.create();
	
	protected static void addHandler(Class<? extends MLPacket> pktClass){
		PacketTypes.put(PacketTypes.size(), pktClass);
	}
	
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		
		MLPacket mlPkt = tryCastPacket(packet, player);
		if (mlPkt != null){
			try {
				if (FMLCommonHandler.instance().getEffectiveSide().isServer()){
					mlPkt.handleServerSide();
				} else {
					mlPkt.handleClientSide();
				}
				
			} catch (Exception e) {
				onError(e, mlPkt);
			}
		} else {
			FMLLog.severe("%s received unknown packet", this.getClass().toString());
		}
	}
	
	protected void onError(Throwable e, MLPacket mlPkt) {
		FMLLog.log("Packet|"+mlPkt.channel, Level.SEVERE, e, "Error handling packet");
	}

	private static MLPacket tryCastPacket(Packet250CustomPayload pkt, Player pl){
		ByteArrayDataInput dat = ByteStreams.newDataInput(pkt.data);
		int pkId = dat.readInt();
		if (PacketTypes.get(pkId) != null){
			try {
				Constructor<? extends MLPacket> contructor = PacketTypes.get(pkId).getConstructor(Player.class, ByteArrayDataInput.class);
				MLPacket nPkt = contructor.newInstance(pl, dat);
				nPkt.channel = pkt.channel;
				return nPkt;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public static Integer findPacketId(Class<? extends MLPacket> pktClass){
		return PacketTypes.inverse().get(pktClass);
	}
}
