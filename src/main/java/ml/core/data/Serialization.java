package ml.core.data;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ml.core.data.serializers.SArray;
import ml.core.data.serializers.SBlockCoord;
import ml.core.data.serializers.SForgeDirection;
import ml.core.data.serializers.SItemsStack;
import ml.core.data.serializers.SNBTTagCompound;
import ml.core.data.serializers.SString;

import com.google.common.io.ByteArrayDataInput;

public class Serialization {
	
	public static final List<IDataSerializer> serializers = new ArrayList<IDataSerializer>();
	static {
		addSerializer(new SString());
		addSerializer(new SNBTTagCompound());
		addSerializer(new SItemsStack());
		addSerializer(new SForgeDirection());
		addSerializer(new SBlockCoord());
		addSerializer(new SArray());
	}
	
	public static void addSerializer(IDataSerializer serializer) {
		serializers.add(serializer);
	}

	public static IDataSerializer getSerializer(Class<?> clazz) {
		IDataSerializer slzr = null;
		for (IDataSerializer IDS : serializers) {
			if (IDS.handles(clazz) && (slzr == null || slzr.getPriority()<IDS.getPriority()))
					slzr = IDS;
		}
		return slzr;
	}
	
	public static void serialize(Class<?> clazz, Object obj, DataOutputStream dataOut) throws IOException {
		if (clazz == int.class || clazz == Integer.class)
			dataOut.writeInt((Integer)obj);
		else if (clazz == boolean.class || clazz == Boolean.class)
			dataOut.writeBoolean((Boolean)obj);
		else if (clazz == double.class || clazz == Double.class)
			dataOut.writeDouble((Double)obj);
		else if (clazz == byte.class || clazz == Byte.class)
			dataOut.writeByte((Byte)obj);
		else if (clazz == short.class || clazz == Short.class)
			dataOut.writeShort((Short)obj);
		else if (clazz == float.class || clazz == Float.class)
			dataOut.writeFloat((Float)obj);
		else if (clazz == long.class || clazz == Long.class)
			dataOut.writeLong((Long)obj);
		
		else {
			IDataSerializer slzr = getSerializer(clazz);
			if (slzr != null) slzr.serialize(obj, dataOut);
			else throw new RuntimeException("Could not find serializer for "+clazz.getName());
		}
	}
	
	public static void serialize(Object obj, DataOutputStream dataOut) throws IOException {
		serialize(obj.getClass(), obj, dataOut);
	}
	
	public static Object deserialize(Class<?> clazz, ByteArrayDataInput dataIn) throws IOException {
		
		if (clazz == int.class || clazz == Integer.class)
			return dataIn.readInt();
		else if (clazz == boolean.class || clazz == Boolean.class)
			return dataIn.readBoolean();
		else if (clazz == double.class || clazz == Double.class)
			return dataIn.readDouble();
		else if (clazz == byte.class || clazz == Byte.class)
			return dataIn.readByte();
		else if (clazz == short.class || clazz == Short.class)
			return dataIn.readShort();
		else if (clazz == float.class || clazz == Float.class)
			return dataIn.readFloat();
		else if (clazz == long.class || clazz == Long.class)
			return dataIn.readLong();
		
		else {
			IDataSerializer slzr = getSerializer(clazz);
			if (slzr != null) return slzr.deserialize(clazz, dataIn);
			else throw new RuntimeException("Could not find serializer for "+clazz.getName());
		}
	}
}
