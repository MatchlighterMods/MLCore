package ml.core.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ml.core.internal.CoreLogger;
import ml.core.vec.Vector2i;
import ml.core.world.feature.IFeatureGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.world.ChunkEvent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class WorldGenHandler implements IWorldGenerator {

	public static final WorldGenHandler instance = new WorldGenHandler();
	private final Multimap<String, IFeatureGenerator> genFeatures = HashMultimap.create();
	private final Multimap<Integer, RetroQueueItem> genQueue = HashMultimap.create();
	
	public static final String rdb_base = "RetrogenData_";
	
	public void registerGenerator(String modId, IFeatureGenerator gen) {
		genFeatures.put(modId, gen);
	}
	
	@SubscribeEvent
	public void handleChunkLoad(ChunkEvent.Load evt) {
		World w = evt.world;
		Chunk c = evt.getChunk();
		if (w.isRemote || !c.isTerrainPopulated) return; //Make sure this is server side and that initial world gen has actually been run
		
		int x = c.xPosition;
		int z = c.zPosition;
		
		int dimid = w.provider.dimensionId;
		
		for (String modId : genFeatures.keySet()) {
			RetroDataBase rdb = getRetroDataBase(w, modId);
			for (IFeatureGenerator feat : genFeatures.get(modId)) {
				if (feat.allowRetroGen(w, x, z) && feat.canGenerateInWorld(w) && rdb.shouldPerformRetroJob(x, z, feat.getGenIdentifier(), feat.getFeatureVersion())) {
					RetroQueueItem rqi = new RetroQueueItem(x, z, w, feat, rdb.getLastFeatureVesrion(x, z, feat.getGenIdentifier()), rdb);
					synchronized(this) {
						CoreLogger.info("Chunk @ ("+x+","+z+",DIM"+w.provider.dimensionId+") has been marked for retroactive feature generation for ("+modId+":"+feat.getGenIdentifier()+")");
						genQueue.put(dimid, rqi);
					}
				}
			}
		}
	}

	public void tickWorld(TickEvent.WorldTickEvent evt) {
		if (evt.phase == Phase.END) {
			World world = evt.world;
			int dimid = world.provider.dimensionId;
			
			synchronized (this) {
				if (genQueue.get(dimid).size() > 0) {
					List<RetroQueueItem> subQ = new ArrayList<WorldGenHandler.RetroQueueItem>(genQueue.removeAll(dimid));
					Iterator<RetroQueueItem> iter = subQ.iterator();
					
					int count=0;
					while (iter.hasNext()) {
						RetroQueueItem rqi = iter.next();
						iter.remove();
						
						long worldSeed = world.getSeed();
						Random rand = new Random(worldSeed);
						long xSeed = rand.nextLong() >> 2 + 1L;
						long zSeed = rand.nextLong() >> 2 + 1L;
						long chunkSeed = (xSeed * rqi.chunkX + zSeed * rqi.chunkZ) ^ worldSeed;
						rand.setSeed(chunkSeed);
						
						rqi.feature.doPopulate(rand, rqi.chunkX, rqi.chunkZ, world, true, rqi.lastVer);
						rqi.rdb.markRetroJobDone(rqi.chunkX, rqi.chunkZ, rqi.feature.getGenIdentifier(), rqi.feature.getFeatureVersion());
						
						count++;
						if (count > 32)
							break;
					}
					
					genQueue.putAll(dimid, subQ);
					CoreLogger.info(count+" chunks have been retro-generated. "+genQueue.get(dimid).size()+" more left.");
				}
			}
		}
	}
	
	/**
	 * Standard, first-time chunk generation.
	 */
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (String modId : genFeatures.keySet()) {
			Collection<IFeatureGenerator> features = genFeatures.get(modId);
			RetroDataBase rdb = getRetroDataBase(world, modId);
			
			for (IFeatureGenerator feature : features) {
				if (feature.canGenerateInWorld(world)) {
					feature.doPopulate(rand, chunkX, chunkZ, world, false, -1);
					rdb.markRetroJobDone(chunkX, chunkZ, feature.getGenIdentifier(), feature.getFeatureVersion());
				}
			}
		}
	}
	
	private RetroDataBase getRetroDataBase(World w, String modId) {
		RetroDataBase rdb = (RetroDataBase)w.perWorldStorage.loadData(RetroDataBase.class, rdb_base+modId);
		if (rdb == null) {
			rdb = new RetroDataBase(rdb_base+modId);
			w.perWorldStorage.setData(rdb_base+modId, rdb);
			CoreLogger.info("Created retro generation database for DIM"+w.provider.dimensionId);
		}
		return rdb;
	}
	
	private static class RetroQueueItem {
		final World w;
		final int chunkX;
		final int chunkZ;
		final int lastVer;
		
		final IFeatureGenerator feature;
		final RetroDataBase rdb;
		
		public RetroQueueItem(int chunkX, int chunkZ, World w, IFeatureGenerator feature, int lastVer, RetroDataBase rdb) {
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
			this.w = w;
			this.feature = feature;
			this.lastVer = lastVer;
			this.rdb = rdb;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof RetroQueueItem) {
				RetroQueueItem r = (RetroQueueItem)obj;
				return r.chunkX == chunkX && r.chunkZ == chunkZ && r.feature == feature && r.w == w;
			}
			return false;
		}
	}
	
	//Credit to ProjectRed for most of this bit.
	public static class RetroDataBase extends WorldSavedData {

		private HashMap<Vector2i, NBTTagCompound> chunks = new HashMap<Vector2i, NBTTagCompound>();
		
		public RetroDataBase(String par1Str) {
			super(par1Str);
		}
		
		public int getLastFeatureVesrion(int chunkX, int chunkZ, String jobId) {
			NBTTagCompound chunk = chunks.get(new Vector2i(chunkX, chunkZ));
			return chunk==null ? 0 : chunk.getInteger(jobId);
		}
		
		public boolean shouldPerformRetroJob(int chunkX, int chunkZ, String jobId, int jlevel) {
			NBTTagCompound chunk = chunks.get(new Vector2i(chunkX, chunkZ));
			return chunk==null || !chunk.hasKey(jobId) || (chunk.getInteger(jobId) < jlevel);
		}
		
		public void markRetroJobDone(int chunkX, int chunkZ, String jobId, int jlevel) {
			NBTTagCompound chunk = chunks.get(new Vector2i(chunkX, chunkZ));
			if (chunk == null)
				chunk = new NBTTagCompound();
			chunk.setInteger(jobId, jlevel);
			chunks.put(new Vector2i(chunkX, chunkZ), chunk);
			markDirty();
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			NBTTagList lst = (NBTTagList)nbt.getTagList("chunks", 10);
			for (int i=0; i<lst.tagCount(); i++) {
				NBTTagCompound tag = lst.getCompoundTagAt(i);
				chunks.put(new Vector2i(tag.getInteger("x"), tag.getInteger("z")), tag.getCompoundTag("manifest"));
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound nbt) {
			NBTTagList lst = new NBTTagList();
			for (Vector2i i : chunks.keySet()) {
				NBTTagCompound tg = new NBTTagCompound();
				tg.setInteger("x", i.x);
				tg.setInteger("z", i.y);
				tg.setTag("manifest", chunks.get(i));
				lst.appendTag(tg);
			}
			nbt.setTag("chunks", lst);
		}
		
	}

}
