package ml.core.vec.transform;

import org.lwjgl.opengl.GL11;

import ml.core.vec.Vector3;

public class Translation extends Transformation {

	public Vector3 vec;
	
	public Translation(double x, double y, double z) {
		this(new Vector3(x,y,z));
	}
	
	public Translation(Vector3 v) {
		vec = v.copy();
	}
	
	@Override
	public void applyTo(Vector3 V) {
		V.add(vec);
	}

	@Override
	public void applyToNormal(Vector3 N) {}

	@Override
	public void applyTo(Matrix4d mat) {
		mat.translate(vec);
	}

	@Override
	public void glTransform() {
		GL11.glTranslated(vec.x, vec.y, vec.z);
	}

}
