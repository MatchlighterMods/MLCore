package ml.core.vec.transform;

import ml.core.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Provides Object Space Transformations.
 * Not that while these can be applied to GL (with glTransform) they will not be ordered the same.
 * GL Transforms are World Spaced; they move the world instead of the object.
 * 
 * Inspired by ChickenBone's CodeChickenLib's implementation
 * @author Matchlighter
 */
public abstract class Transformation {

	public abstract void applyTo(Vector3 V);
	
	public abstract void applyToNormal(Vector3 N);
	
	public abstract void applyTo(Matrix4d mat);
	
	public MultiTransformation append(Transformation t) {
		return new MultiTransformation(new Transformation[]{this, t});
	}
	
	public LocalizedTransformation localize(Vector3 norig) {
		return new LocalizedTransformation(norig, this);
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void glTransform();
}
