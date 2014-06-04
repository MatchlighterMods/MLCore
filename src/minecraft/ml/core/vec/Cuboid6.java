package ml.core.vec;

import ml.core.vec.transform.Transformation;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

public class Cuboid6 {
	
	public Vector3d min;
	public Vector3d max;
	
	public Cuboid6(Vector3d imin, Vector3d imax) {
		min = imin.copy();
		max = imax.copy();
	}
	
	public Cuboid6(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		this(new Vector3d(minx, miny, minz), new Vector3d(maxx, maxy, maxz));
	}
	
	public Cuboid6(AxisAlignedBB aabb){
		this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
	}
	
	public Cuboid6 translate(Vector3d v3){
		min = min.add(v3);
		max = max.add(v3);
		return this;
	}
	
	public void setToBlockBounds(Block blk){
		blk.setBlockBounds((float)min.x, (float)min.y, (float)min.z, (float)max.x, (float)max.y, (float)max.z);
	}
	
	public Cuboid6 transform(Transformation t) {
		Vector3d nmin = t.getTransformedPoint(min);
		Vector3d nmax = t.getTransformedPoint(max);
		
		double minx = nmin.x;
		double miny = nmin.y;
		double minz = nmin.z;
		              
		double maxx = nmax.x;
		double maxy = nmax.y;
		double maxz = nmax.z;
		
		double tempx = minx;
		double tempy = miny;
		double tempz = minz;
		
		if (maxx < minx) { minx = maxx; maxx = tempx; }
		if (maxy < miny) { miny = maxy; maxy = tempy; }
		if (maxz < minz) { minz = maxz; maxz = tempz; }
		
		this.min = new Vector3d(minx, miny, minz);
		this.max = new Vector3d(maxx, maxy, maxz);
		
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cuboid6){
			Cuboid6 asC6 = (Cuboid6)obj;
			return (asC6.min.equals(min)) && (asC6.max.equals(max));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return min.hashCode()*31 + max.hashCode();
	}

}
