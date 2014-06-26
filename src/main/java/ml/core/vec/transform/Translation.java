package ml.core.vec.transform;

import ml.core.vec.Vector3d;

import org.lwjgl.opengl.GL11;

public class Translation extends Transformation {

	public Vector3d vec;
	
	public Translation(double x, double y, double z) {
		this(new Vector3d(x,y,z));
	}
	
	public Translation(Vector3d v) {
		vec = v.copy();
	}
	
	@Override
	public Vector3d getTransformedPoint(Vector3d point) {
		return point.add(vec);
	}

	@Override
	public Vector3d getTransformedVector(Vector3d vector) {
		return vector;
	}

	@Override
	public void getTransformedMatrix(TransformationMatrix mat) {
		mat.translate(vec);
	}

	@Override
	public void glTransform() {
		GL11.glTranslated(vec.x, vec.y, vec.z);
	}

}
