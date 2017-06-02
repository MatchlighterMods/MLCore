package ml.core.texture;

import ml.core.block.BlockUtils;
import ml.core.block.BlockUtils.SpatialRelation;
import ml.core.vec.BlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ConnectedTexture {

	private BlockCoord co = new BlockCoord(0, 0, 0);
	private static final SpatialRelation[] sides = {SpatialRelation.Above, SpatialRelation.Right, SpatialRelation.Below, SpatialRelation.Left};
	
	public int getTextureData(IBlockAccess iba, int x, int y, int z, int face) {
		ForgeDirection fface = ForgeDirection.getOrientation(face);
		byte data = 0; //0b00000000
		for (int s=0; s<sides.length; s++) {
			SpatialRelation sr = sides[s];
			ForgeDirection mside = BlockUtils.getFDFromFaceRelation(fface, sr);
			ForgeDirection toL = BlockUtils.getFDFromFaceRelation(fface, sides[(s+3)%4]); //mside.getRotation(fface);
			
			if (!faceCanConnectOnSide(iba, co.set(x,y,z), mside, face)) {
				data |= 3 << s*2 | 2 << ((s+1)%4)*2; //0b11
			} else if (!faceCanConnectOnSide(iba, co.set(x,y,z).translate(toL), mside, face) || !faceCanConnectOnSide(iba, co.set(x,y,z).translate(mside), toL, face)) {
				data |= 2 << s*2; //0b10
			}
		}
		return mapTexture(data);
	}
	
	/**
	 * Maps data to an index of the sprite it is associated with.
	 * Typical use cases can use {@link ConnectedTexture#ctmMap} for mapping.
	 */
	public int mapTexture(byte data) {
		return ctmMap[data];
	}
	
	public abstract boolean faceCanConnectOnSide(IBlockAccess iba, BlockCoord co, ForgeDirection side, int face);
	
	public static int[] defaultIndices = {0,1,2,3,4,5,6,7,8,9,10,11, 16,17,18,19,20,21,22,23,24,25,26,27, 32,33,34,35,36,37,38,39,40,41,42,43, 48,49,50,51,53,54,55,56,57,58,59};
	
	protected static byte[] ctmMap = new byte[256];
	static { //Why does it need Java 7 for binary literals...? Anyone need a script for converting binary literals found in a file? I've got one.
		ctmMap[0] = 17;
		ctmMap[255] = 51; //0b11111111
		
		//Single Edges
		ctmMap[11] = 1; //0b00001011
		ctmMap[44] = 18; //0b00101100
		ctmMap[176] = 33; //0b10110000
		ctmMap[194] = 16; //0b11000010
		
		//Single Corners
		ctmMap[203] = 0; //0b11001011
		ctmMap[47] = 2; //0b00101111
		ctmMap[242] = 32; //0b11110010
		ctmMap[188] = 34; //0b10111100
		
		//Double Corners
		ctmMap[191] = 50; //0b10111111
		ctmMap[251] = 48; //0b11111011
		ctmMap[239] = 3; //0b11101111
		ctmMap[254] = 35; //0b11111110
		
		//Parallel Edges
		ctmMap[187] = 49; //0b10111011
		ctmMap[238] = 19; //0b11101110
		
		//-------------------------
		//Dots
		ctmMap[170] = 21; //0b10101010
		
		//Triple Dots
		ctmMap[168] = 9; //0b10101000
		ctmMap[162] = 10; //0b10100010
		ctmMap[138] = 26; //0b10001010
		ctmMap[42] = 25; //0b00101010
		
		//Single Edges - Double Dots
		ctmMap[171] = 5; //0b10101011
		ctmMap[174] = 22; //0b10101110
		ctmMap[186] = 37; //0b10111010
		ctmMap[234] = 20; //0b11101010
		
		//No Edges - Double dots -6
		ctmMap[10] = 27; //0b00001010
		ctmMap[160] = 11; //0b10100000
		ctmMap[40] = 53; //0b00101000
		ctmMap[130] = 54; //0b10000010
		ctmMap[34] = 43; //0b00100010
		ctmMap[136] = 59; //0b10001000
		
		//No Edges - Single Dots -4
		ctmMap[2] = 24; //0b00000010
		ctmMap[8] = 23; //0b00001000
		ctmMap[32] = 7; //0b00100000
		ctmMap[128] = 8; //0b10000000
		
		//Single Edges - Single Dots -8
		ctmMap[184] = 55; //0b10111000
		ctmMap[178] = 56; //0b10110010
		ctmMap[43] = 39; //0b00101011
		ctmMap[139] = 40; //0b10001011
		
		ctmMap[226] = 41; //0b11100010
		ctmMap[202] = 57; //0b11001010
		ctmMap[172] = 42; //0b10101100
		ctmMap[46] = 58; //0b00101110
		
		//Single Corner - Single Dot -4
		ctmMap[235] = 4; //0b11101011
		ctmMap[175] = 6; //0b10101111
		ctmMap[250] = 36; //0b11111010
		ctmMap[190] = 38; //0b10111110
	}
}
