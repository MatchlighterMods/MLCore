package ml.core.data.serializers;

import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

import ml.core.data.IDataSerializer;
import ml.core.vec.BlockCoord;

public class SBlockCoord implements IDataSerializer {

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz == BlockCoord.class;
	}

	@Override
	public Object deserialize(Class<?> clazz, ByteArrayDataInput dIn) throws IOException {
		return new BlockCoord(dIn.readInt(), dIn.readInt(), dIn.readInt());
	}

	@Override
	public void serialize(Object obj, DataOutputStream dOut) throws IOException {
		BlockCoord bc = (BlockCoord)obj;
		dOut.writeInt(bc.x);
		dOut.writeInt(bc.y);
		dOut.writeInt(bc.z);
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
