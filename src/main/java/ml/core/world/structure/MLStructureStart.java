package ml.core.world.structure;

import java.util.Random;

import ml.core.world.structure.MLStructureComponent.InitialStructureComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

public abstract class MLStructureStart extends StructureStart {

	public MLStructureStart() {}
	
	public MLStructureStart(InitialStructureComponent initialComponent, World world, Random rnd, int chunkX, int chunkZ) {
		super(chunkX, chunkZ);
		
		this.components.add(initialComponent);
		initialComponent.buildComponent(initialComponent, this.components, rnd);
		
		while (!initialComponent.unbuiltComponents.isEmpty()) {
			//int i = rnd.nextInt(initialComponent.unbuiltComponents.size());
			MLStructureComponent nextComponent = initialComponent.unbuiltComponents.remove(0);
			nextComponent.buildComponent(initialComponent, this.components, rnd);
			
			if (this.components.contains(nextComponent)) this.components.add(nextComponent);
		}
		
		updateBoundingBox();
	}
	
}
