package ml.core.internal;

import java.io.IOException;
import java.util.Iterator;

import ml.core.gui.core.MLContainer;
import ml.core.network.MLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

public class PacketContainerData extends MLPacket {
	
	public static final LinkedListMultimap<EntityPlayer, PacketContainerData> packetQ = LinkedListMultimap.create();
	
	public @data int winId;
	public @data NBTTagCompound payload;

	public PacketContainerData(EntityPlayer pl, ByteArrayDataInput dataIn) {
		super(dataIn);
	}
	
	public PacketContainerData(int windowId, NBTTagCompound payload) {
		super("MLCore");
		this.winId = windowId;
		this.payload = payload;
	}

	@Override
	public void handleClientSide(EntityPlayer epl) throws IOException {
		handle(epl, Side.CLIENT);
	}
	
	@Override
	public void handleServerSide(EntityPlayer epl) throws IOException {
		handle(epl, Side.SERVER);
	}
	
	public void handle(EntityPlayer epl, Side side) throws IOException {
		if (epl.openContainer instanceof MLContainer && epl.openContainer.windowId == winId) {
			((MLContainer)epl.openContainer).handleDataPacket(payload, side);
		} else {
			packetQ.put(epl, this);
			Iterator<PacketContainerData> i = packetQ.values().iterator();
			while (i.hasNext()) {
				if (i.next().winId != this.winId) {
					i.remove();
				}
			}
		}
	}
}
