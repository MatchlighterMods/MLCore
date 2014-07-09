package ml.core.data.serializers;

import java.io.DataOutputStream;
import java.io.IOException;

import ml.core.data.IDataSerializer;

import com.google.common.io.ByteArrayDataInput;

public class SString implements IDataSerializer {

	@Override
	public boolean handles(Class<?> clazz) {
		return clazz==String.class;
	}

	@Override
	public Object deserialize(Class<?> clazz, ByteArrayDataInput dIn) throws IOException {
		return readString(dIn, 256);
	}

	@Override
	public void serialize(Object obj, DataOutputStream dOut) throws IOException {
		writeString(dOut, (String)obj);
	}

	@Override
	public int getPriority() {
		return 0;
	}

	public static String readString(ByteArrayDataInput dataIn, Integer maxLength) throws IOException {
		short var2 = dataIn.readShort();

		if (var2 > maxLength){
			throw new IOException("Received string length longer than maximum allowed (" + var2 + " > " + maxLength + ")");
		}else if (var2 < 0){
			throw new IOException("Received string length is less than zero! Weird string!");
		}else{
			StringBuilder var3 = new StringBuilder();

			for (int var4 = 0; var4 < var2; ++var4){
				var3.append(dataIn.readChar());
			}

			return var3.toString();
		}
	}

	public static void writeString(DataOutputStream dataOut, String par0Str) throws IOException {
		if (par0Str.length() > 32767){
			throw new IOException("String too big");
		}else{
			dataOut.writeShort(par0Str.length());
			dataOut.writeChars(par0Str);
		}
	}

}
