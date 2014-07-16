package ml.core.vec;

import java.util.Arrays;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public class Vector3d {

	public final double x;
	public final double y;
	public final double z;
	
	public static Vector3d fromForgeDir(ForgeDirection fd) {
		return new Vector3d(fd.offsetX, fd.offsetY, fd.offsetZ);
	}
	
	public Vector3d(double xin, double yin, double zin) {
		x = xin;
		y = yin;
		z = zin;
	}
	
	public Vector3d(Vector3d c) {
		x=c.x;
		y=c.y;
		z=c.z;
	}

	public Vector3d copy() {
		return new Vector3d(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector3d){
			Vector3d asV3 = (Vector3d)obj;
			return asV3.x == x && asV3.y == y && asV3.z == z;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new double[]{x,y,z});
	}

	public double dot(Vector3d ov3) {
		return this.x*ov3.x + this.y*ov3.y + this.z*ov3.z;
	}
	
	public Vector3d cross(Vector3d ov3) {
		return new Vector3d(y * ov3.z - z * ov3.y,
				z - ov3.x - x * ov3.z,
				x - ov3.y - y * ov3.x);
	}

	public double magnitude() {
		return Math.sqrt(magSqr());
	}

	public double magSqr() {
		return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
	}

	public Vector3d normalize() {
		double mag = magnitude();
		if (mag==0)
			return new Vector3d(0, 0, 0);
		return divide(mag);
	}

	public Vector3d minus(Vector3d dif) {
		return new Vector3d(x-dif.x, y-dif.y, z-dif.z);
	}

	public Vector3d add(Vector3d a) {
		return new Vector3d(x+a.x, y+a.y, z+a.z);
	}

	public Vector3d divide(double div) {
		return new Vector3d(x / div, y / div, z / div);
	}

	public Vector3d mult(double fac) {
		return new Vector3d(x * fac, y * fac, z * fac);
	}
	
	public Vector3d mult(Vector3d a) {
		return new Vector3d(x*a.x, y*a.y, z*a.z);
	}
	
	public Vector3d negate() {
		return new Vector3d(-x, -y, -z);
	}
	
	// From Apache Commons Math
	public Vector3d getOrthogonal() {
		double threshold = 0.6 * magnitude();
		
        if (Math.abs(x) <= threshold) {
            double inverse  = 1 / Math.sqrt(y * y + z * z);
            return new Vector3d(0, inverse * z, -inverse * y);
        } else if (Math.abs(y) <= threshold) {
            double inverse  = 1 / Math.sqrt(x * x + z * z);
            return new Vector3d(-inverse * z, 0, inverse * x);
        }
        double inverse  = 1 / Math.sqrt(x * x + y * y);
        
        return new Vector3d(inverse * y, -inverse * x, 0);
	}

	public Vec3 toVec3() {
		return Vec3.createVectorHelper(x, y, z);
	}

	@Override
	public String toString() {
		return "{"+x+", "+y+", "+z+"}";
	}
}
