package ml.core.vec;

public class Ray {

	public final Vector3d origin;
	public final Vector3d normal;
	
	public Ray(Vector3d origin, Vector3d normal) {
		this.origin = origin;
		this.normal = normal;
	}
	
}
