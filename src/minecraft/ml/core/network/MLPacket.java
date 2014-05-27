package ml.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

import ml.core.data.IDataSerializer;
import ml.core.data.Serialization;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public abstract class MLPacket {
	
	/**
	 * Can be applied to any field that has a Type that has a serializer.
	 * See {@link MLPacket#serializers}, {@link IDataSerializer}, and examples in the serializers package
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface data {}
	
	protected int packetID;
	
	// Incoming
	
	public MLPacket(EntityPlayer pl, ByteArrayDataInput dataIn) {
		try {
			Field[] flds = this.getClass().getFields();
			Arrays.sort(flds, fldComparator);
			for (Field fld : flds) {
				data ann = fld.getAnnotation(data.class);
				if (ann != null) {
					Class cls = fld.getType();
					Object v = Serialization.deserialize(cls, dataIn);
					fld.set(this, v);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void handle(EntityPlayer epl, Side side) throws IOException {
		if (side == Side.CLIENT) {
			handleClientSide(epl);
		} else {
			handleServerSide(epl);
		}
	}
	
	public void handleClientSide(EntityPlayer epl) throws IOException {};
	
	public void handleServerSide(EntityPlayer epl) throws IOException {};
	
	// Outgoing
	public boolean chunkDataPacket = true;
	public String channel;
	
	public MLPacket(String ch) {
		packetID = PacketHandler.findPacketId(this.getClass());
		channel = ch;
	}
	
	public Packet250CustomPayload convertToPkt250() {
		ByteArrayOutputStream dataOutRaw = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(dataOutRaw);
		
		try {
			dataOut.writeInt(packetID);
			
			Field[] flds = this.getClass().getFields();
			Arrays.sort(flds, fldComparator);
			for (Field fld : flds) {
				data ann = fld.getAnnotation(data.class);
				if (ann != null) {
					Class cls = fld.getType();
					Serialization.serialize(cls, fld.get(this), dataOut);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload mcPkt = new Packet250CustomPayload(channel, dataOutRaw.toByteArray());
		mcPkt.isChunkDataPacket = chunkDataPacket;
		return mcPkt;
	}
	
	public void dispatchToServer() {
		PacketDispatcher.sendPacketToServer(convertToPkt250());
	}
	
	public void dispatchToPlayer(EntityPlayer epl) {
		PacketDispatcher.sendPacketToPlayer(convertToPkt250(), (Player)epl);
	}
	
	public void dispatchToDimension(int dimId) {
		PacketDispatcher.sendPacketToAllInDimension(convertToPkt250(), dimId);
	}
	
	public void dispatchToAllNear(double X, double Y, double Z, double range, int dimensionId) {
		PacketDispatcher.sendPacketToAllAround(X, Y, Z, range, dimensionId, convertToPkt250());
	}
	
	public void dispatchToAll() {
		PacketDispatcher.sendPacketToAllPlayers(convertToPkt250());
	}
	
	private Comparator<Field> fldComparator = new Comparator<Field>() {
		@Override
		public int compare(Field arg0, Field arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	};
}
