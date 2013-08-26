package ml.core.vec.transform;

import java.util.ArrayList;

import ml.core.vec.Matrix;
import ml.core.vec.Vector3;

public class MultiTransformation extends Transformation {

	private ArrayList<Transformation> tranforms = new ArrayList<Transformation>();
	private Matrix4d cacheMat;
	
	public MultiTransformation(Transformation[] trans) {
		for (Transformation t : trans)
			tranforms.add(t);
	}
	
	@Override
	public MultiTransformation append(Transformation t) {
		cacheMat = null;
		tranforms.add(t);
		return this;
	}
	
	public Matrix4d toMatrix() {
		if (cacheMat==null) {
			cacheMat = new Matrix4d();
			for (Transformation t : tranforms)
				t.applyTo(cacheMat);
		}
		return cacheMat;
	}

	@Override
	public void applyTo(Vector3 V) {
		for (Transformation t : tranforms)
			t.applyTo(V);
	}

	@Override
	public void applyToNormal(Vector3 N) {
		for (Transformation t : tranforms)
			t.applyToNormal(N);
	}

	@Override
	public void applyTo(Matrix4d mat) {
		mat.mult(toMatrix());
	}

	@Override
	public void glTransform() {
		for (int i=tranforms.size()-1; i>=0; i--) {
			tranforms.get(i).glTransform();
		}
	}

}
