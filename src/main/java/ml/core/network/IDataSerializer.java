package ml.core.network;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;

public interface IDataSerializer {

	public boolean handles(Class clazz);

	public Object deserialize(ByteArrayDataInput dIn, EntityPlayer epl) throws IOException;

	public void serialize(Object obj, DataOutputStream dOut) throws IOException;

	public int getPriority();

}