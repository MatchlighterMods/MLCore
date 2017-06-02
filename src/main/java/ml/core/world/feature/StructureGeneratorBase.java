package ml.core.world.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.MapGenStructure;

public abstract class StructureGeneratorBase extends MapGenStructure implements IFeatureGenerator {

	@Override
	public boolean doPopulate(Random rand, int chunkX, int chunkZ, World world, boolean isRetroGen, int oldVersion) {
		generate(world.getChunkProvider(), world, chunkX, chunkZ, null);
		return generateStructuresInChunk(world, rand, chunkX, chunkZ);
	}

	public void generate(IChunkProvider par1iChunkProvider, World world, int chunkX, int chunkZ, Block[] par5ArrayOfByte) {
		if (canGenerateInWorld(world))
			super.generate(par1iChunkProvider, world, chunkX, chunkZ, par5ArrayOfByte);
	}
	
	@Override
	public String getGenIdentifier() {
		return getStructureName();
	}

}
