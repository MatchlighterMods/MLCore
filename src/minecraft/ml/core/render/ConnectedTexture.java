package ml.core.render;

import net.minecraft.world.IBlockAccess;

public abstract class ConnectedTexture {

	
	public int getTextureData(IBlockAccess iba, int x, int y, int z, int face) {
		int[][] sData = {{}};
		
		int data = 0x000000;
		for (int side=0; side<4; side++) {
			
		}
	}
	
	public abstract boolean faceCanConnectOnSide(IBlockAccess iba, int x, int y, int z, int side, int face);
}
