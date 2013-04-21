package ml.core.network;

import java.io.IOException;

import ml.core.tile.IRotatableTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public abstract class PacketDescribe extends MLPacket {

	public int x;
	public int y;
	public int z;
	public ForgeDirection facing;

	public PacketDescribe(TileEntity te) {
		super(null);

		x = te.xCoord;
		y = te.yCoord;
		z = te.zCoord;
		facing = (te instanceof IRotatableTE) ? ((IRotatableTE)te).getFacing() : ForgeDirection.UNKNOWN;

		writeInt(x);
		writeInt(y);
		writeInt(z);
		writeInt(facing.ordinal());
	}

	public PacketDescribe(Player pl, ByteArrayDataInput data) {
		super(pl, data);

		x = dataIn.readInt();
		y = dataIn.readInt();
		z = dataIn.readInt();
		facing = ForgeDirection.getOrientation(dataIn.readInt());
	}

	@Override
	public void handleClientSide() throws IOException{
		EntityPlayer asEntPl = (EntityPlayer)player;
		TileEntity te = asEntPl.worldObj.getBlockTileEntity(x, y, z);
		
		if (te instanceof IRotatableTE){
			((IRotatableTE)te).setFacing(facing);
		}
		
		handleClientSide(te);
	}
	
	public abstract void handleClientSide(TileEntity te) throws IOException;

	@Override
	public void handleServerSide() throws IOException {};

}
