package ml.core.data.serializers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

import ml.core.data.IDataSerializer;
import ml.core.data.Serialization;

import com.google.common.io.ByteArrayDataInput;

public class SArray implements IDataSerializer {

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz.isArray();
	}

	@Override
	public Object deserialize(Class<?> clazz, ByteArrayDataInput dIn) throws IOException {
		Class<?> cls = clazz.getComponentType();
		
		int len = dIn.readInt();
		Object arr = Array.newInstance(cls, len);
		for (int i=0; i<len; i++) {
			Array.set(arr, i, Serialization.deserialize(cls, dIn));
		}
		return arr;
	}

	@Override
	public void serialize(Object array, DataOutputStream dataOut) throws IOException {
		int len = Array.getLength(array);
		dataOut.writeInt(len);
		for (int i=0; i<len; i++) {
			Object aobj = Array.get(array, i);
			Serialization.serialize(aobj, dataOut);
		}
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
