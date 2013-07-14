package ml.core.network.serializers;

import java.io.DataOutputStream;
import java.io.IOException;

import ml.core.network.IDataSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

public class STileEntity implements IDataSerializer {

	@Override
	public boolean handles(Class clazz) {
		return clazz==TileEntity.class || TileEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public Object deserialize(ByteArrayDataInput dIn, EntityPlayer epl)
			throws IOException {
		return epl.worldObj.getBlockTileEntity(dIn.readInt(), dIn.readInt(), dIn.readInt());
	}

	@Override
	public void serialize(Object obj, DataOutputStream dOut) throws IOException {
		TileEntity te = (TileEntity)obj;
		dOut.writeInt(te.xCoord);
		dOut.writeInt(te.yCoord);
		dOut.writeInt(te.zCoord);
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
