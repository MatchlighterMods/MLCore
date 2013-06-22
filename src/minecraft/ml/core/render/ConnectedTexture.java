package ml.core.render;

import ml.core.block.BlockUtils;
import ml.core.block.BlockUtils.SpatialRelation;
import ml.core.geo.BlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.IBlockLiquid.BlockType;

public abstract class ConnectedTexture {

	private BlockCoord co = new BlockCoord(0, 0, 0);
	private static final SpatialRelation[] sides = {SpatialRelation.Above, SpatialRelation.Right, SpatialRelation.Below, SpatialRelation.Left};
	
	public int getTextureData(IBlockAccess iba, int x, int y, int z, int face) {
		ForgeDirection fface = ForgeDirection.getOrientation(face);
		int data = 0; //0b00000000;
		for (int s=0; s<sides.length; s++) {
			SpatialRelation sr = sides[s];
			ForgeDirection mside = BlockUtils.getFDFromFaceRelation(fface, sr);
			ForgeDirection toL = BlockUtils.getFDFromFaceRelation(fface, sides[(s+3)%4]); //mside.getRotation(fface);
			
			if (!faceCanConnectOnSide(iba, co.set(x,y,z), mside, face)) {
				data |= 0b11 << s*2 | 0b10 << ((s+1)%4)*2;
			} else if (!faceCanConnectOnSide(iba, co.set(x,y,z).translate(toL), mside, face) || !faceCanConnectOnSide(iba, co.set(x,y,z).translate(mside), toL, face)) {
				data |= 0b10 << s*2;
			}
		}
		return mapTexture(data);
	}
	
	public abstract int mapTexture(int data);
	public abstract boolean faceCanConnectOnSide(IBlockAccess iba, BlockCoord co, ForgeDirection side, int face);
	
	protected static int[] ctmMap = new int[256];
	static { //Why does it need Java 7 for binary literals...?
		ctmMap[0] = 17;
		ctmMap[0b11111111] = 51;
		
		//Single Edges
		ctmMap[0b00001011] = 1;
		ctmMap[0b00101100] = 18;
		ctmMap[0b10110000] = 33;
		ctmMap[0b11000010] = 16;
		
		//Single Corners
		ctmMap[0b11001011] = 0;
		ctmMap[0b00101111] = 2;
		ctmMap[0b11110010] = 32;
		ctmMap[0b10111100] = 34;
		
		//Double Corners
		ctmMap[0b10111111] = 50;
		ctmMap[0b11111011] = 48;
		ctmMap[0b11101111] = 3;
		ctmMap[0b11111110] = 35;
		
		//Parallel Edges
		ctmMap[0b10111011] = 49;
		ctmMap[0b11101110] = 19;
		
		//-------------------------
		//Dots
		ctmMap[0b10101010] = 21;
		
		//Triple Dots
		ctmMap[0b10101000] = 9;
		ctmMap[0b10100010] = 10;
		ctmMap[0b10001010] = 26;
		ctmMap[0b00101010] = 25;
		
		//Single Edges - Double Dots
		ctmMap[0b10101011] = 5;
		ctmMap[0b10101110] = 22;
		ctmMap[0b10111010] = 37;
		ctmMap[0b11101010] = 20;
		
		//No Edges - Double dots -6
		ctmMap[0b00001010] = 27;
		ctmMap[0b10100000] = 11;
		ctmMap[0b00101000] = 53;
		ctmMap[0b10000010] = 54;
		ctmMap[0b00100010] = 43;
		ctmMap[0b10001000] = 59;
		
		//No Edges - Single Dots -4
		ctmMap[0b00000010] = 24;
		ctmMap[0b00001000] = 23;
		ctmMap[0b00100000] = 7;
		ctmMap[0b10000000] = 8;
		
		//Single Edges - Single Dots -8
		ctmMap[0b10111000] = 55;
		ctmMap[0b10110010] = 56;
		ctmMap[0b00101011] = 39;
		ctmMap[0b10001011] = 40;
		
		ctmMap[0b11100010] = 41;
		ctmMap[0b11001010] = 57;
		ctmMap[0b10101100] = 42;
		ctmMap[0b00101110] = 58;
		
		//Single Corner - Single Dot -4
		ctmMap[0b11101011] = 4;
		ctmMap[0b10101111] = 6;
		ctmMap[0b11111010] = 36;
		ctmMap[0b10111110] = 38;
	}
}
