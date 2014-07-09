package ml.core.data.serializers;

import java.io.DataOutputStream;
import java.io.IOException;

import ml.core.data.IDataSerializer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

public class SNBTTagCompound implements IDataSerializer {

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz == NBTTagCompound.class;
	}

	@Override
	public Object deserialize(Class<?> clazz, ByteArrayDataInput dIn) throws IOException {
		return readNBTTagCompound(dIn);
	}

	@Override
	public void serialize(Object obj, DataOutputStream dOut) throws IOException {
		writeNBTTagCompound(dOut, (NBTTagCompound)obj);
	}

	@Override
	public int getPriority() {
		return 0;
	}

	public static NBTTagCompound readNBTTagCompound(ByteArrayDataInput dataIn) throws IOException
	{
		short var1 = dataIn.readShort();

		if (var1 < 0) {
			return null;
		} else {
			byte[] var2 = new byte[var1];
			dataIn.readFully(var2);
			return CompressedStreamTools.decompress(var2);
		}
	}

	public static void writeNBTTagCompound(DataOutputStream dataOut, NBTTagCompound par0NBTTagCompound) throws IOException {
		if (par0NBTTagCompound == null) {
			dataOut.writeShort(-1);
		} else {
			byte[] var2 = CompressedStreamTools.compress(par0NBTTagCompound);
			dataOut.writeShort((short)var2.length);
			dataOut.write(var2);
		}
	}

}
