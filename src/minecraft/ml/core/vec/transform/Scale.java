package ml.core.vec.transform;

import org.lwjgl.opengl.GL11;

import ml.core.vec.Vector3;

public class Scale extends Transformation {

	public Vector3 factor;
	
	public Scale(Vector3 fac) {
		this.factor = fac.copy();
	}
	
	public Scale(double fac) {
		this(new Vector3(fac, fac, fac));
	}

	@Override
	public void applyTo(Vector3 V) {
		V.mult(factor);
	}

	@Override
	public void applyToNormal(Vector3 N) {}

	@Override
	public void applyTo(Matrix4d mat) {
		mat.scale(factor);
	}

	@Override
	public void glTransform() {
		GL11.glScaled(factor.x, factor.y, factor.z);
	}

}
