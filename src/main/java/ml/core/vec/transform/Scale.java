package ml.core.vec.transform;

import ml.core.vec.Vector3d;

import org.lwjgl.opengl.GL11;

public class Scale extends Transformation {

	public Vector3d factor;
	
	public Scale(Vector3d fac) {
		this.factor = fac.copy();
	}
	
	public Scale(double fac) {
		this(new Vector3d(fac, fac, fac));
	}

	@Override
	public Vector3d getTransformedPoint(Vector3d point) {
		return point.mult(factor);
	}

	@Override
	public Vector3d getTransformedVector(Vector3d vector) {
		return vector;
	}

	@Override
	public void getTransformedMatrix(TransformationMatrix mat) {
		mat.scale(factor);
	}

	@Override
	public void glTransform() {
		GL11.glScaled(factor.x, factor.y, factor.z);
	}

}
