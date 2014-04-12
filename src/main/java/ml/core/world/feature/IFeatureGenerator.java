package ml.core.world.feature;

import java.util.Random;

import net.minecraft.world.World;

public interface IFeatureGenerator {

	public boolean doGeneration(Random rand, int chunkX, int chunkZ, World world, boolean isRetroGen, int oldVersion);
	
	public String getSubIdentifier();
	
	public int getFeatureVersion();
	
	public boolean allowRetroGen();
}
