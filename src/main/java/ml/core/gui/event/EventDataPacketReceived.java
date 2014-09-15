package ml.core.gui.event;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import ml.core.gui.core.GuiElement;

public class EventDataPacketReceived extends GuiEvent {
	
	public NBTTagCompound payload;
	public Side side;

	public EventDataPacketReceived(GuiElement origin, NBTTagCompound data, Side side) {
		super(origin);
		this.payload = data;
		this.side = side;
	}

}
