package ml.core.vec;

public class Plane {

	public final Vector3d origin;
	public final Vector3d normal;
	private Vector3d u;
	private Vector3d v;
	
	public Plane(Vector3d origin, Vector3d normal, Vector3d u, Vector3d v) {
		this.origin = origin;
		this.normal = normal;
		
		this.u = u;
		this.v = v;
	}
	
	public Plane(Vector3d origin, Vector3d normal) {
		this.origin = origin;
		this.normal = normal;
		
		this.u = normal.getOrthogonal();
		this.v = normal.cross(u);
	}
	
	public Plane(Vector3d p1, Vector3d p2, Vector3d p3) {
		Vector3d dS21 = p2.minus(p1);
		Vector3d dS31 = p3.minus(p1);
		this.origin = p1;
		this.normal = dS21.cross(dS31);
		
		this.u = p2.normalize();
		this.v = normal.cross(u);
	}
	
	public boolean pointOnPlane(Vector3d point) {
		return Math.abs(normal.dot(point.minus(origin))) < 1e-6D;
	}
	
	public Vector2d toPlanarSpace(Vector3d vec) {
		vec = vec.minus(origin);
		return new Vector2d(vec.dot(u), vec.dot(v));
	}
	
	public Vector3d toWorldSpace(Vector2d vec) {
		double x = vec.x * u.x + vec.y * v.x + origin.x;
		double y = vec.x * u.y + vec.y * v.y + origin.y;
		double z = vec.x * u.z + vec.y * v.z + origin.z;
		return new Vector3d(x, y, z);
	}
	
}
