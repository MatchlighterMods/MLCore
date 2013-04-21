package ml.core.geo;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class Cuboid6 {
	
	public Vector3 min;
	public Vector3 max;
	
	public Cuboid6(Vector3 imin, Vector3 imax) {
		min = imin;
		max = imax;
	}
	
	public Cuboid6(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		this(new Vector3(minx, miny, minz), new Vector3(maxx, maxy, maxz));
	}
	
	public Cuboid6(AxisAlignedBB aabb){
		this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
	}
	
	public Cuboid6 translate(Vector3 v3){
		min = min.add(v3);
		max = max.add(v3);
		return this;
	}
	
	public void setToBlockBounds(Block blk){
		blk.setBlockBounds((float)min.x, (float)min.y, (float)min.z, (float)max.x, (float)max.y, (float)max.z);
	}
}
