package ml.core.data.serializers;

import java.io.DataOutputStream;
import java.io.IOException;

import ml.core.data.IDataSerializer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class STileEntity implements IDataSerializer {

	@Override
	public boolean handles(Class clazz) {
		return clazz==TileEntity.class || TileEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public Object deserialize(Class clazz, ByteArrayDataInput dIn) throws IOException {
		World world;
		int dimId = dIn.readInt();
		System.out.println(dimId);
		if (FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT &&
				FMLClientHandler.instance().getClient().theWorld.provider.dimensionId == dimId) { // This is a bit hacky, but it shouldn't ever be a problem
			world = FMLClientHandler.instance().getClient().theWorld;
		} else
			world = DimensionManager.getWorld(dimId);
		
		if (world == null) return null;
		return world.getBlockTileEntity(dIn.readInt(), dIn.readInt(), dIn.readInt());
	}

	@Override
	public void serialize(Object obj, DataOutputStream dOut) throws IOException {
		TileEntity te = (TileEntity)obj;
		dOut.writeInt(te.worldObj.provider.dimensionId);
		dOut.writeInt(te.xCoord);
		dOut.writeInt(te.yCoord);
		dOut.writeInt(te.zCoord);
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
