package ml.core.world.structure;


public class WeightedComponent {

	public Class<? extends MLStructureComponent> cls;
	public final int componentWeight;
	public int instancesCreated;
	public int maxComponentInstances;

	public WeightedComponent(Class<? extends MLStructureComponent> cls, int weight, int maxPieces) {
		this.cls = cls;
		this.componentWeight = weight;
		this.maxComponentInstances = maxPieces;
	}

}
