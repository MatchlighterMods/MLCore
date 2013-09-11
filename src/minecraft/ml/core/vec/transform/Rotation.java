package ml.core.vec.transform;

import ml.core.vec.Vector3;

import org.lwjgl.opengl.GL11;

public class Rotation extends Transformation {

	public Vector3 axis;
	public double degrees;
	
	public Rotation(Vector3 axis, double degs) {
		this.axis = axis;
		this.degrees = degs;
	}

	private Matrix4d getMatrix() {
		return new Matrix4d().rotateDegs(axis, degrees);
	}
	
	@Override
	public void applyTo(Vector3 V) {
		getMatrix().applyTo(V);
	}

	@Override
	public void applyToNormal(Vector3 N) {
		getMatrix().applyToNormal(N);
	}
	
	@Override
	public void applyTo(Matrix4d mat) {
		mat.mult(getMatrix());
	}

	@Override
	public void glTransform() {
		GL11.glRotated(degrees, axis.x, axis.y, axis.z);
	}

}
