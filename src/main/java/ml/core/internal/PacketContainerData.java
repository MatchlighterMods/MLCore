package ml.core.internal;

import java.io.IOException;

import ml.core.gui.core.MLContainer;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class PacketContainerData extends MLPacket {
	
	public @data int winId;
	public @data NBTTagCompound payload;

	public PacketContainerData(EntityPlayer pl, ByteArrayDataInput dataIn) {
		super(pl, dataIn);
	}
	
	public PacketContainerData(int windowId, NBTTagCompound payload) {
		super("MLCore");
		this.winId = windowId;
		this.payload = payload;
	}

	@Override
	public void handle(EntityPlayer epl, Side side) throws IOException {
		if (epl.openContainer instanceof MLContainer && epl.openContainer.windowId == winId) {
			((MLContainer)epl.openContainer).handleDataPacket(payload, side);
		}
	}
}
