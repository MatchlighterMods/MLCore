package ml.core.vec.transform;

import java.util.ArrayList;

import ml.core.vec.Vector3d;

/**
 * Adding transformations here will ensure they are performed in the correct order in GL
 * 
 * @author Matchlighter
 */
public class MultiTransformation extends Transformation {

	private ArrayList<Transformation> tranforms = new ArrayList<Transformation>();
	private TransformationMatrix cacheMat;
	
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
	
	public TransformationMatrix toMatrix() {
		if (cacheMat==null) {
			cacheMat = new TransformationMatrix();
			for (int i=tranforms.size()-1; i>=0; i--)
				tranforms.get(i).applyTo(cacheMat);
		}
		return cacheMat;
	}

	@Override
	public void applyTo(Vector3d V) {
		for (Transformation t : tranforms)
			t.applyTo(V);
	}

	@Override
	public void applyToNormal(Vector3d N) {
		for (Transformation t : tranforms)
			t.applyToNormal(N);
	}

	@Override
	public void applyTo(TransformationMatrix mat) {
		mat.mult(toMatrix());
	}

	@Override
	public void glTransform() {
		for (int i=tranforms.size()-1; i>=0; i--) {
			tranforms.get(i).glTransform();
		}
	}

}
