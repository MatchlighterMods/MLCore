package ml.core.block;

import ml.core.vec.Vector3d;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockUtils {

	public static int[] atCCW = new int[]{4,5,4,5,3,2, 6};
	public static int[] atCW = new int[]{5,4,5,4,2,3, 6};
	public static int[][] relations = {
		{3,2,4,5, 1,0},
		{3,2,4,5, 0,1},
		{0,1,5,4, 3,2},
		{0,1,4,5, 2,3},
		{0,1,2,3, 5,4},
		{0,1,3,2, 4,5},
	};
	
	public enum SpatialRelation {
		Below,
		Above,
		Left,
		Right,
		Behind,
		InFront,;
	}
	
	public static ForgeDirection getFDFromFaceRelation(ForgeDirection face, SpatialRelation rel) {
		switch (rel) {
		case Behind:
			return face.getOpposite();
		case InFront:
			return face;
		default:
			return ForgeDirection.values()[relations[face.ordinal()][rel.ordinal()]];
		}
	}
	
	/**
	 * Gets the y-axis rotation for a {@link ForgeDirection}
	 * Useful for {@link TileEntitySpecialRenderer}s when your block has a rotation
	 * Set so that a model's Z- will be facing the input direction. i.e. Z- is the front 
	 * 
	 * Note: In a default blender setup, Z- is Y(Grn)+
	 * 
	 * @param fd Input {@link ForgeDirection}
	 * @return Angle in degrees for the {@link ForgeDirection}
	 */
	public static float getRotationFromDirection(ForgeDirection fd){
		switch (fd){
		case NORTH:
			return 0F;
		case SOUTH:
			return 180F;
		case WEST:
			return 90F;
		case EAST:
			return 270F;
		}
		return 0F;
	}
	
	/**
	 * Rotates the current glMatrix so that Z- faces the specified {@link ForgeDirection}
	 * 
	 * Note: In a default blender setup, Z- is Y(Grn)+
	 * 
	 * @param fd The {@link ForgeDirection} Z- should face
	 */
	@SideOnly(Side.CLIENT)
	public static void glRotateForFaceDir(ForgeDirection fd){
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		switch (fd){
		case UP:
			GL11.glRotatef(90F, 1.0F, 0F, 0F);
			break;
		case DOWN:
			GL11.glRotatef(-90F, 1.0F, 0F, 0F);
			break;
		default:
			GL11.glRotatef(BlockUtils.getRotationFromDirection(fd), 0F, 1.0F, 0F);
		}
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	}
	
	/**
	 * Gets the ForgeDirection that is counter-clockwise from the input
	 */
	public static ForgeDirection getCClockwise(ForgeDirection fd){
		return ForgeDirection.getOrientation(atCCW[fd.ordinal()]);
	}
	
	/**
	 * Gets the ForgeDirection that is clockwise from the input
	 */
	public static ForgeDirection getClockwise(ForgeDirection fd){
		return ForgeDirection.getOrientation(atCW[fd.ordinal()]);
	}
	
	/**
	 * Gets the {@link ForgeDirection} that is closest to facing the player.
	 * Assumes all {@link ForgeDirection} (except UNKNOWN) are accepted.
	 * 
	 * @param placer The entity placing the block
	 * @param x The x-coord of the block being placed
	 * @param y The y-coord of the block being placed
	 * @param z	The z-coord of the block being placed
	 * @return The nearest allowed {@link ForgeDirection}
	 */
	public static ForgeDirection getPlacedForgeDir(Entity placer, int x, int y, int z){
		return getPlacedForgeDir(placer, x, y, z, ForgeDirection.VALID_DIRECTIONS);
	}
	
	/**
	 * Gets the {@link ForgeDirection} that is closest to facing the player.
	 * May be a bit overkill... but eh.
	 * 
	 * @param placer The entity placing the block
	 * @param x The x-coord of the block being placed
	 * @param y The y-coord of the block being placed
	 * @param z	The z-coord of the block being placed
	 * @param allowedDirs Array of {@link ForgeDirection} that are acceptable as returns
	 * @return The nearest allowed {@link ForgeDirection}
	 */
	public static ForgeDirection getPlacedForgeDir(Entity placer, int x, int y, int z, ForgeDirection[] allowedDirs){
		
		Vector3d look = (new Vector3d(placer.posX, placer.posY+placer.height/2, placer.posZ).minus(new Vector3d((float)x+0.5F, (float)y+0.5F, (float)z+0.5F))).normalize(); 
		
		ForgeDirection cfd = ForgeDirection.UNKNOWN;
		double loang = Math.PI;
		for (ForgeDirection fd : allowedDirs){
			double secDot = Math.acos(look.dot(new Vector3d(fd.offsetX, fd.offsetY, fd.offsetZ)));
			if (secDot<loang){
				loang = secDot;
				cfd = fd;
			}
		}
		
		return cfd;
	}
}
