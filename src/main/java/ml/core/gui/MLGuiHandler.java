package ml.core.gui;

import ml.core.gui.core.TopParentGuiElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class MLGuiHandler implements IGuiHandler {

	public abstract TopParentGuiElement getTopElement(int ID, EntityPlayer player, World world, int x, int y, int z, Side side);
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TopParentGuiElement tel = getTopElement(ID, player, world, x, y, z, Side.SERVER);
		if (tel == null)
			return null;
		
		tel.initControls();
		tel.getContainer().initMissingHotbarSlots();
		tel.getContainer().checkPacketQueue();
		
		return tel.getContainer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			return getServerGuiElement(ID, player, world, x, y, z);
		
		TopParentGuiElement tel = getTopElement(ID, player, world, x, y, z, Side.CLIENT);
		if (tel == null)
			return null;
		
		tel.initControls();
		tel.constructClient();
		tel.getContainer().initMissingHotbarSlots();
		tel.getContainer().checkPacketQueue();
		tel.guiTick();
		
		return tel.getGui();
	}

}
