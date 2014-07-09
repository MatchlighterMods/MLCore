package ml.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public abstract class MLPacket {
	
	/**
	 * Can be applied to any field that has a Type that has a serializer.
	 * See {@link MLPacket#serializers}, {@link IDataSerializer}, and examples in the serializers package
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface data {}
	
	protected int packetID;
	
	// Incoming
	
	public MLPacket(ByteArrayDataInput dataIn) {
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
	
	public void handleClientSide(EntityPlayer epl) throws IOException {};
	
	public void handleServerSide(EntityPlayer epl) throws IOException {};
	
	// Outgoing
	public boolean chunkDataPacket = true;
	public String channel;
	
	public MLPacket(String ch) {
		packetID = PacketHandler.findPacketId(this.getClass());
		channel = ch;
	}
	
	public FMLProxyPacket convertToFMLPacket() {
		ByteBuf buf = Unpooled.buffer();
//		ByteArrayOutputStream dataOutRaw = new ByteArrayOutputStream();
//		DataOutputStream dataOut = new DataOutputStream(dataOutRaw);
		
		try {
			dataOut.writeInt(packetID);
			
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
		
		return new FMLProxyPacket(buf, channel);
	}
	
	private Comparator<Field> fldComparator = new Comparator<Field>() {
		@Override
		public int compare(Field arg0, Field arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	};
}
