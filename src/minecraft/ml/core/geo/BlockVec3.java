package ml.core.geo;

public class BlockVec3 {
	public int x;
	public int y;
	public int z;
	
	public BlockVec3(int ix, int iy, int iz) {
		x = ix;
		y = iy;
		z = iz;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockVec3){
			BlockVec3 asV3 = (BlockVec3)obj;
			return asV3.x == x && asV3.y == y && asV3.z == z;
		}
		return false;
	}

	public double magnitude(){
		return Math.sqrt(magSqr());
	}

	public int magSqr(){
		return x*x+y*y+z*z;
	}
}
