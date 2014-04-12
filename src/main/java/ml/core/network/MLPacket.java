package ml.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ml.core.network.serializers.SForgeDirection;
import ml.core.network.serializers.SItemsStack;
import ml.core.network.serializers.SNBTTagCompound;
import ml.core.network.serializers.SString;
import ml.core.network.serializers.STileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public abstract class MLPacket {
	
	public static final List<IDataSerializer> serializers = new ArrayList<IDataSerializer>();
	static {
		serializers.add(new SString());
		serializers.add(new SNBTTagCompound());
		serializers.add(new SItemsStack());
		serializers.add(new SForgeDirection());
		serializers.add(new STileEntity());
	}
	
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
				
					if (cls == int.class || cls == Integer.class)
						fld.set(this, dataIn.readInt());
					else if (cls == boolean.class || cls == Boolean.class)
						fld.set(this, dataIn.readBoolean());
					else if (cls == double.class || cls == Double.class)
						fld.set(this, dataIn.readDouble());
					else if (cls == byte.class || cls == Byte.class)
						fld.set(this, dataIn.readByte());
					
					else {
						IDataSerializer slzr = null;
						for (IDataSerializer IDS : serializers) {
							if (IDS.handles(cls) && (slzr == null || slzr.getPriority()<IDS.getPriority()))
								slzr = IDS;
						} 
						if (slzr != null) fld.set(this, slzr.deserialize(dataIn, pl)); else throw new RuntimeException("Could not find serializer for "+cls.getName());
					}
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
				
					if (cls == int.class || cls == Integer.class)
						dataOut.writeInt((Integer)fld.get(this));
					else if (cls == boolean.class || cls == Boolean.class)
						dataOut.writeBoolean((Boolean)fld.get(this));
					else if (cls == double.class || cls == Double.class)
						dataOut.writeDouble((Double)fld.get(this));
					else if (cls == byte.class || cls == Byte.class)
						dataOut.writeByte((Byte)fld.get(this));
					
					else {
						IDataSerializer slzr = null;
						for (IDataSerializer IDS : serializers) {
							if (IDS.handles(cls) && (slzr == null || slzr.getPriority()<IDS.getPriority()))
									slzr = IDS;
						}
						if (slzr != null) slzr.serialize(fld.get(this), dataOut); else throw new RuntimeException("Could not find serializer for "+cls.getName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload mcPkt = new Packet250CustomPayload(channel, dataOutRaw.toByteArray());
		mcPkt.isChunkDataPacket = chunkDataPacket;
		return mcPkt;
	}
	
	private Comparator<Field> fldComparator = new Comparator<Field>() {
		@Override
		public int compare(Field arg0, Field arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	};
}
