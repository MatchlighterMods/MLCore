package ml.core.network;

import java.io.IOException;

import ml.core.tile.TileEntityConnectable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketDescribeConnectable extends PacketDescribe {

	ForgeDirection linkDir;
	
	public PacketDescribeConnectable(TileEntityConnectable tec, String ch) {
		super(tec, ch);
		linkDir = tec.linkedDir;
		
		writeInt(linkDir.ordinal());
	}
	
	public PacketDescribeConnectable(Player pl, ByteArrayDataInput data) {
		super(pl, data);
		
		linkDir = ForgeDirection.getOrientation(data.readInt());
	}
	
	@Override
	public void handleClientSide(TileEntity te) throws IOException {
		TileEntityConnectable tec = (TileEntityConnectable)te;

		tec.linkedDir = linkDir;
	}
}
