package ml.core.network.serializers;

import java.io.DataOutputStream;
import java.io.IOException;

import ml.core.network.IDataSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

public class SForgeDirection implements IDataSerializer {

	@Override
	public boolean handles(Class clazz) {
		return clazz == ForgeDirection.class;
	}

	@Override
	public Object deserialize(ByteArrayDataInput dIn, EntityPlayer epl) throws IOException {
		return ForgeDirection.values()[dIn.readInt()];
	}

	@Override
	public void serialize(Object obj, DataOutputStream dOut) throws IOException {
		dOut.writeInt(((ForgeDirection)obj).ordinal());
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
