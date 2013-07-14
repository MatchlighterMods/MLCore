package ml.core.network;

import java.io.IOException;

import ml.core.tile.IRotatableTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

@Deprecated
public abstract class PacketDescribe extends MLPacket {

	public @data int x;
	public @data int y;
	public @data int z;
	public @data ForgeDirection facing;

	public PacketDescribe(TileEntity te, String ch) {
		super(ch);

		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
		facing = (te instanceof IRotatableTE) ? ((IRotatableTE)te).getFacing() : ForgeDirection.UNKNOWN;

	}

	public PacketDescribe(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);

	}

	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException{
		TileEntity te = epl.worldObj.getBlockTileEntity(x, y, z);
		
		if (te instanceof IRotatableTE){
			((IRotatableTE)te).setFacing(facing);
		}
		
		handleClientSide(te, epl);
	}
	
	public abstract void handleClientSide(TileEntity te, EntityPlayer epl) throws IOException;

	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {};

}
