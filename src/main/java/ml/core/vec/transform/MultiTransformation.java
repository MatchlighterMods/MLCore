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
				tranforms.get(i).getTransformedMatrix(cacheMat);
		}
		return cacheMat;
	}

	@Override
	public Vector3d getTransformedPoint(Vector3d point) {
		for (Transformation t : tranforms)
			point = t.getTransformedPoint(point);
		return point;
	}

	@Override
	public Vector3d getTransformedVector(Vector3d vector) {
		for (Transformation t : tranforms)
			vector = t.getTransformedVector(vector);
		return vector;
	}

	@Override
	public void getTransformedMatrix(TransformationMatrix mat) {
		mat.mult(toMatrix());
	}

	@Override
	public void glTransform() {
		for (int i=tranforms.size()-1; i>=0; i--) {
			tranforms.get(i).glTransform();
		}
	}

}
