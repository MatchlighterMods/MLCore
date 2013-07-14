package ml.core.network;

import java.io.IOException;

import ml.core.tile.TileEntityConnectable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

/**
 * Can be extended or left untouched.
 * You do not need to add this to your PacketHandler unless you extend it; MLCore features a minimal PacketHandler.
 */
@Deprecated
public class PacketDescribeConnectable extends PacketDescribe {

	public @data ForgeDirection linkDir;
	
	public PacketDescribeConnectable(TileEntityConnectable tec, String ch) {
		super(tec, ch);
		linkDir = tec.linkedDir;
		
	}
	
	public PacketDescribeConnectable(EntityPlayer pl, ByteArrayDataInput data) {
		super(pl, data);
		
	}
	
	@Override
	public void handleClientSide(TileEntity te, EntityPlayer epl) throws IOException {
		TileEntityConnectable tec = (TileEntityConnectable)te;

		tec.linkedDir = linkDir;
	}
}
