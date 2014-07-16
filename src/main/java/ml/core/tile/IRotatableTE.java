package ml.core.tile;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRotatableTE {

	public ForgeDirection getFacing();
	
	public void setFacing(ForgeDirection fd);
	
	public ForgeDirection[] getValidFacingDirections();
}
