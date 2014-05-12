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
	public void applyTo(Vector3d V) {
		V.add(vec);
	}

	@Override
	public void applyToNormal(Vector3d N) {}

	@Override
	public void applyTo(TransformationMatrix mat) {
		mat.translate(vec);
	}

	@Override
	public void glTransform() {
		GL11.glTranslated(vec.x, vec.y, vec.z);
	}

}
