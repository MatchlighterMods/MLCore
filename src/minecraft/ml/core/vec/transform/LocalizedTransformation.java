package ml.core.vec.transform;

import org.lwjgl.opengl.GL11;

import ml.core.vec.Vector3;

public class LocalizedTransformation extends Transformation {

	public Vector3 loc;
	public Transformation baseTrans;
	
	public LocalizedTransformation(Vector3 off, Transformation t) {
		loc = off;
		baseTrans = t;
	}
	
	@Override
	public void applyTo(Vector3 V) {
		V.minus(loc);
		baseTrans.applyTo(V);
		V.add(loc);
	}

	@Override
	public void applyToNormal(Vector3 N) {
		baseTrans.applyToNormal(N);
	}

	@Override
	public void applyTo(Matrix4d mat) {
		mat.translate(loc.copy().negate());
		baseTrans.applyTo(mat);
		mat.translate(loc);
	}

	@Override
	public void glTransform() {
		GL11.glTranslated(loc.x, loc.y, loc.z);
		baseTrans.glTransform();
		GL11.glTranslated(-loc.x, -loc.y, -loc.z);
	}

}
