package ml.core.vec.transform;

import ml.core.vec.Vector3d;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Provides Object Space Transformations.
 * Not that while these can be applied to GL (with glTransform) they will not be ordered the same.
 * GL Transforms are World Spaced; they move the world instead of the object.
 * Use {@link MultiTransformation} to apply Multiple transformations in the correct order.
 * 
 * Inspired by ChickenBones' CodeChickenLib's implementation
 * @author Matchlighter
 */
public abstract class Transformation {

	public abstract Vector3d getTransformedPoint(Vector3d point);
	
	public abstract Vector3d getTransformedVector(Vector3d vector);
	
	public abstract void getTransformedMatrix(TransformationMatrix mat);
	
	public MultiTransformation append(Transformation t) {
		return new MultiTransformation(new Transformation[]{this, t});
	}
	
	public LocalizedTransformation localize(Vector3d norig) {
		return new LocalizedTransformation(norig, this);
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void glTransform();
}
