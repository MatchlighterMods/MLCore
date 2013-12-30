package ml.core.world;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ml.core.vec.Vector2i;
import ml.core.world.feature.IFeatureGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.TickType;

public class WorldGenHandler implements IWorldGenerator, ITickHandler {

	public static WorldGenHandler instance = new WorldGenHandler();
	public Map<String, ArrayList<IFeatureGenerator>> featGenerators = new HashMap<String, ArrayList<IFeatureGenerator>>();
	
	public static final String rdb_base = "RetrogenData_";
	
	public Map<Integer, ArrayList<RetroQueueItem>> retroGenQueue = new HashMap<Integer, ArrayList<RetroQueueItem>>();
	
	public void registerGenerator(String modId, IFeatureGenerator gen) {
		if (!featGenerators.containsKey(modId))
			featGenerators.put(modId, new ArrayList<IFeatureGenerator>());
		
		ArrayList<IFeatureGenerator> gens = featGenerators.get(modId);
		gens.add(gen);
	}
	
	@ForgeSubscribe
	public void handleChunkLoad(ChunkEvent.Load evt) {
		World w = evt.world;
		Chunk c = evt.getChunk();
		if (w.isRemote || !c.isTerrainPopulated) return; //Make sure this is server side and that initial world gen has actually been run
		
		int x = c.xPosition;
		int z = c.zPosition;
		
		int dimid = w.provider.dimensionId;
		ArrayList<RetroQueueItem> rqis = retroGenQueue.get(dimid);
		if (rqis == null)
			rqis = new ArrayList<WorldGenHandler.RetroQueueItem>();
		
		for (String mId : featGenerators.keySet()) {
			RetroDataBase rdb = getRetroDataBase(w, mId);
			for (IFeatureGenerator feat : featGenerators.get(mId)) {
				if (rdb.shouldPerformRetroJob(x, z, feat.getSubIdentifier(), feat.getFeatureVersion()) && feat.allowRetroGen()) {
					FMLLog.info("Chunk @ ("+x+","+z+",DIM"+w.provider.dimensionId+") has been marked for retroactive feature generation for ("+mId+":"+feat.getSubIdentifier()+")");
					rqis.add(new RetroQueueItem(x, z, w, feat, rdb.getLastFeatureVesrion(x, z, feat.getSubIdentifier()), rdb));
					rdb.markRetroJobDone(x, z, feat.getSubIdentifier(), feat.getFeatureVersion());
				}
			}
		}
		retroGenQueue.put(dimid, rqis);
	}

	/**
	 * Standard, first-time chunk generation.
	 */
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (String modId : featGenerators.keySet()) {
			ArrayList<IFeatureGenerator> features = featGenerators.get(modId);
			RetroDataBase rdb = getRetroDataBase(world, modId);
			for (IFeatureGenerator feature : features) {
				System.out.println("Natural Gen");
				feature.doGeneration(rand, chunkX, chunkZ, world, false, -1);
				rdb.markRetroJobDone(chunkX, chunkZ, feature.getSubIdentifier(), feature.getFeatureVersion());
			}
		}
	}
	
	private RetroDataBase getRetroDataBase(World w, String modId) {
		RetroDataBase rdb = (RetroDataBase)w.perWorldStorage.loadData(RetroDataBase.class, rdb_base+modId);
		if (rdb == null) {
			rdb = new RetroDataBase(rdb_base+modId);
			w.perWorldStorage.setData(rdb_base+modId, rdb);
			FMLLog.info("Created retro generation database for DIM"+w.provider.dimensionId);
		}
		return rdb;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		World world = (World)tickData[0];
		int dimid = world.provider.dimensionId;
		
		ArrayList<RetroQueueItem> rqis = retroGenQueue.get(dimid);
		
		if (rqis != null && rqis.size() > 0) {
			rqis = (ArrayList)rqis.clone();
			ArrayList<RetroQueueItem> removing_rqis = new ArrayList<WorldGenHandler.RetroQueueItem>();
			
			int count=0;
			for (RetroQueueItem rqi : rqis) {
				
				long worldSeed = world.getSeed();
				Random rand = new Random(worldSeed);
				long xSeed = rand.nextLong() >> 2 + 1L;
				long zSeed = rand.nextLong() >> 2 + 1L;
				long chunkSeed = (xSeed * rqi.chunkX + zSeed * rqi.chunkZ) ^ worldSeed;
				rand.setSeed(chunkSeed);
				
				rqi.feature.doGeneration(rand, rqi.chunkX, rqi.chunkZ, world, true, rqi.lastVer);
					//rqi.rdb.markRetroJobDone(rqi.chunkX, rqi.chunkZ, rqi.feature.getSubIdentifier(), rqi.feature.getFeatureVersion());
				
				removing_rqis.add(rqi);
				count++;
				if (count > 32)
					break;
			}
			retroGenQueue.get(dimid).removeAll(removing_rqis);
			FMLLog.info(count+" chunks have been retro-generated. "+retroGenQueue.get(dimid).size()+" more left.");
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "MLCore:WorldGen";
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
			NBTTagList lst = (NBTTagList)nbt.getTagList("chunks");
			for (int i=0; i<lst.tagCount(); i++) {
				NBTTagCompound tag = (NBTTagCompound)lst.tagAt(i);
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
				tg.setCompoundTag("manifest", chunks.get(i));
				lst.appendTag(tg);
			}
			nbt.setTag("chunks", lst);
		}
		
	}

}
