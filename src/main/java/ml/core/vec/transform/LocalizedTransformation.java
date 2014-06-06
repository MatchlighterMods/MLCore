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
	public Vector3d getTransformedPoint(Vector3d point) {
		return baseTrans.getTransformedPoint(point.minus(loc));
	}

	@Override
	public Vector3d getTransformedVector(Vector3d vector) {
		return baseTrans.getTransformedVector(vector);
	}

	@Override
	public void getTransformedMatrix(TransformationMatrix mat) {
		mat.translate(loc.copy().negate());
		baseTrans.getTransformedMatrix(mat);
		mat.translate(loc);
	}

	@Override
	public void glTransform() {
		GL11.glTranslated(loc.x, loc.y, loc.z);
		baseTrans.glTransform();
		GL11.glTranslated(-loc.x, -loc.y, -loc.z);
	}

}
