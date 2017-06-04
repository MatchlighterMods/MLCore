package ml.core.data;

import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

public interface IDataSerializer {

	public boolean handles(Class<?> clazz);

	public Object deserialize(Class<?> clazz, ByteArrayDataInput dIn) throws IOException;

	public void serialize(Object obj, DataOutputStream dOut) throws IOException;

	public int getPriority();

}