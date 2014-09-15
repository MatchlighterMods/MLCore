package ml.core.vec;

import ml.core.math.MathUtils;

public class Intersect {

	public static Vector3d intersectPlane(Ray ray, Plane plane) {
		Vector3d dR = ray.normal; //ray.origin.minus(ray.normal);
		
		double ndRDot = plane.normal.dot(dR);
		if (Math.abs(ndRDot) < 1e-6D) return null;
		
		double t = -plane.normal.dot(ray.origin.minus(plane.origin)) / ndRDot;
		Vector3d ipnt = ray.origin.add(dR.mult(t));
		
		return ipnt;
	}
	
	public static boolean intersectsPolygon(Ray ray, Plane plane, Polygon pgon) {
		Vector3d planeInt = intersectPlane(ray, plane);
		if (planeInt == null) return false;
		
		Vector2d planar = plane.toPlanarSpace(planeInt);
		System.out.println(planeInt);
		System.out.println(planar);
		return pgon.pointInPolygon(planar);
	}
	
	public static boolean intersectsRect(Ray ray, Vector3d cOrigin, Vector3d cHoriz, Vector3d cVert) {
		double aMag = cHoriz.minus(cOrigin).magnitude();
		double dMag = cVert.minus(cOrigin).magnitude();
		return intersectsPolygon(ray, new Plane(cOrigin, cHoriz, cVert), 
				new Polygon(new Vector2d(0,0), new Vector2d(aMag,0), new Vector2d(aMag,dMag), new Vector2d(0,dMag)));
	}
	
	/**
	 * Returns a new vector with x value equal to the second parameter, along the line between this vector and the
	 * passed in vector, or null if not possible.
	 */
	public static Vector3d getInterceptOfYZ(Vector3d start, Vector3d end, double tX) {
		double delX = end.x - start.x;
		double delY = end.y - start.y;
		double delZ = end.z - start.z;

		if (delX == 0) return null;
		double d = (tX - start.x) / delX;

		if (MathUtils.between(-1E-5, delX, 1E-5))
			return start;

		return new Vector3d(tX, start.y + delY * d, start.z + delZ * d);
	}

	/**
	 * Returns a new vector with y value equal to the second parameter, along the line between this vector and the
	 * passed in vector, or null if not possible.
	 */
	public static Vector3d getInterceptOfXZ(Vector3d start, Vector3d end, double tY) {
		double delX = end.x - start.x;
		double delY = end.y - start.y;
		double delZ = end.z - start.z;

		if (delY == 0) return null;
		double d = (tY - start.y) / delY;

		if (MathUtils.between(-1E-5, delY, 1E-5))
			return start;

		return new Vector3d(start.x + delX * d, tY, start.z + delZ * d);
	}

	/**
	 * Returns a new vector with z value equal to the second parameter, along the line between this vector and the
	 * passed in vector, or null if not possible.
	 */
	public static Vector3d getInterceptOfXY(Vector3d start, Vector3d end, double tZ) {
		double delX = end.x - start.x;
		double delY = end.y - start.y;
		double delZ = end.z - start.z;

		if (delZ == 0) return null;
		double d = (tZ - start.z) / delZ;

		if (MathUtils.between(-1E-5, delZ, 1E-5))
			return start;

		return new Vector3d(start.x + delX * d, start.y + delY * d, tZ);
	}
	
}
