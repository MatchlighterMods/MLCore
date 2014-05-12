package ml.core.vec.transform;

import ml.core.vec.Vector3d;

import org.lwjgl.opengl.GL11;

public class LocalizedTransformation extends Transformation {

	public Vector3d loc;
	public Transformation baseTrans;
	
	public LocalizedTransformation(Vector3d off, Transformation t) {
		loc = off;
		baseTrans = t;
	}
	
	@Override
	public void applyTo(Vector3d V) {
		V.minus(loc);
		baseTrans.applyTo(V);
		V.add(loc);
	}

	@Override
	public void applyToNormal(Vector3d N) {
		baseTrans.applyToNormal(N);
	}

	@Override
	public void applyTo(TransformationMatrix mat) {
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
