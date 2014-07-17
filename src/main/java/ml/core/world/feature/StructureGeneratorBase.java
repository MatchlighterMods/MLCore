package ml.core.world.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.MapGenStructure;

public abstract class StructureGeneratorBase extends MapGenStructure implements IFeatureGenerator {

	@Override
	public boolean doPopulate(Random rand, int chunkX, int chunkZ, World world, boolean isRetroGen, int oldVersion) {
		func_151539_a(world.getChunkProvider(), world, chunkX, chunkZ, null);
		return generateStructuresInChunk(world, rand, chunkX, chunkZ);
	}
	
	@Override
	public void func_151539_a(IChunkProvider par1iChunkProvider, World world, int chunkX, int chunkZ, Block[] blocks) {
		if (canGenerateInWorld(world))
			super.func_151539_a(par1iChunkProvider, world, chunkX, chunkZ, blocks);
	}
	
	@Override
	public String getGenIdentifier() {
		return func_143025_a();
	}

}
