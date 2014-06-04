package ml.core.vec.transform;

import ml.core.vec.Vector3d;

import org.lwjgl.opengl.GL11;

public class Rotation extends Transformation {

	public Vector3d axis;
	public double degrees;
	
	public Rotation(Vector3d axis, double degs) {
		this.axis = axis;
		this.degrees = degs;
	}

	private TransformationMatrix getMatrix() {
		return new TransformationMatrix().rotateDegs(axis, degrees);
	}
	
	@Override
	public Vector3d getTransformedPoint(Vector3d point) {
		return getMatrix().getTransformedPoint(point);
	}

	@Override
	public Vector3d getTransformedVector(Vector3d vector) {
		return getMatrix().getTransformedVector(vector);
	}
	
	@Override
	public void getTransformedMatrix(TransformationMatrix mat) {
		mat.mult(getMatrix());
	}

	@Override
	public void glTransform() {
		GL11.glRotated(degrees, axis.x, axis.y, axis.z);
	}

}
