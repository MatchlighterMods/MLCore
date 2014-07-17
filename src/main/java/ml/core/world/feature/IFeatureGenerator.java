package ml.core.world.feature;

import java.util.Random;

import net.minecraft.world.World;

public interface IFeatureGenerator {

	public boolean doPopulate(Random rand, int chunkX, int chunkZ, World world, boolean isRetroGen, int oldVersion);
	
	public String getGenIdentifier();
	
	public int getFeatureVersion();
	
	public boolean allowRetroGen(World world, int chunkX, int chunkZ);

	public abstract boolean canGenerateInWorld(World world);
}
