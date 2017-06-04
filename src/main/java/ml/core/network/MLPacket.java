package ml.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.Unpooled;
import ml.core.data.IDataSerializer;
import ml.core.data.Serialization;
import net.minecraft.entity.player.EntityPlayer;

public abstract class MLPacket {
	
	/**
	 * Can be applied to any field that has a Type that has a serializer.
	 * See {@link MLPacket#serializers}, {@link IDataSerializer}, and examples in the serializers package
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface data {}
	
	// Incoming
	
	public MLPacket(EntityPlayer pl, ByteArrayDataInput dataIn) {
		try {
			Field[] flds = this.getClass().getFields();
			Arrays.sort(flds, fldComparator);
			for (Field fld : flds) {
				data ann = fld.getAnnotation(data.class);
				if (ann != null) {
					Class<?> cls = fld.getType();
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
	
	public MLPacket() {}
	
	public FMLProxyPacket convertToFMLProxyPacket(PacketHandler pkh) {
		ByteArrayOutputStream dataOutRaw = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(dataOutRaw);
		
		
		try {
			dataOut.writeInt(pkh.findPacketId(this.getClass()));
			
			Field[] flds = this.getClass().getFields();
			Arrays.sort(flds, fldComparator);
			for (Field fld : flds) {
				data ann = fld.getAnnotation(data.class);
				if (ann != null) {
					Class<?> cls = fld.getType();
					Serialization.serialize(cls, fld.get(this), dataOut);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FMLProxyPacket mcPkt = new FMLProxyPacket(Unpooled.wrappedBuffer(dataOutRaw.toByteArray()), pkh.channel);
//		mcPkt.isChunkDataPacket = chunkDataPacket;
		return mcPkt;
	}
	
//	public void dispatchToServer() {
//		PacketDispatcher.sendPacketToServer(convertToPkt250());
//	}
//	
//	public void dispatchToPlayer(EntityPlayer epl) {
//		PacketDispatcher.sendPacketToPlayer(convertToPkt250(), (Player)epl);
//	}
//	
//	public void dispatchToDimension(int dimId) {
//		PacketDispatcher.sendPacketToAllInDimension(convertToPkt250(), dimId);
//	}
//	
//	public void dispatchToAllNear(double X, double Y, double Z, double range, int dimensionId) {
//		PacketDispatcher.sendPacketToAllAround(X, Y, Z, range, dimensionId, convertToPkt250());
//	}
//	
//	public void dispatchToAll() {
//		PacketDispatcher.sendPacketToAllPlayers(convertToPkt250());
//	}
	
	private Comparator<Field> fldComparator = new Comparator<Field>() {
		@Override
		public int compare(Field arg0, Field arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	};
}
