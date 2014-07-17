package ml.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Partially inspired by OpenComputers.
 * 
 * @author Matchlighter
 */
public class BlockDelegator <DCls extends DelegateBlock> extends Block {
	
	private TreeMap<Integer, DCls> subBlocks = new TreeMap<Integer, DCls>();
	
	//private BiMap<Integer, DCls> subBlocks = HashBiMap.create();
	
	protected DCls nullDelegate;

	public BlockDelegator(Material xMaterial, DCls nullDelegate) {
		super(xMaterial);
		this.nullDelegate = nullDelegate;
		this.nullDelegate.parent = this;
	}
	
	/* ---------------------------- SubBlocks ---------------------------- */
	
	public boolean addSubBlock(int meta, DCls sub) {
		if (subBlock(meta) != nullDelegate || subBlocks.containsValue(sub)) return false;
		subBlocks.put(meta, sub);
		sub.parent = this;
		sub.metaId = meta;
		return true;
	}
	
	public DCls subBlock(int meta) {
		if (subBlocks.containsKey(meta)) return subBlocks.get(meta);
		Entry<Integer, DCls> ent = subBlocks.floorEntry(meta);
		if (ent != null && (ent.getValue().getMetaId() == meta || meta < ent.getValue().getMetaId()+ent.getValue().getMetaLength())) {
			return ent.getValue();
		}
		return nullDelegate;
	}
	
	public DCls subBlock(ItemStack is) {
		if (is != null && is.getItem() instanceof ItemBlock) {
			ItemBlock ib = (ItemBlock)is.getItem();
			Block blk = ib.field_150939_a;
			if (blk == this) {
				return subBlock(is.getItemDamage());
			}
		}
		return nullDelegate;
	}
	
	public DCls subBlock(IBlockAccess world, int x, int y, int z) {
		Block blk = world.getBlock(x, y, z);
		if (blk == this) {
			return subBlock(world.getBlockMetadata(x, y, z));
		}
		return nullDelegate;
	}
	
	public static DelegateBlock findSubBlock(ItemStack is) {
		if (is != null && is.getItem() instanceof ItemBlock) {
			ItemBlock ib = (ItemBlock)is.getItem();
			Block blk = ib.field_150939_a;
			if (blk instanceof BlockDelegator<?>) {
				return ((BlockDelegator<?>)blk).subBlock(is.getItemDamage());
			}
		}
		return null;
	}
	
	public static DelegateBlock findSubBlock(IBlockAccess world, int x, int y, int z) {
		Block blk = world.getBlock(x, y, z);
		if (blk instanceof BlockDelegator<?>) {
			return ((BlockDelegator<?>)blk).subBlock(world.getBlockMetadata(x, y, z));
		}
		return null;
	}
	
	/* ---------------------------- ItemMethods ---------------------------- */
	
	public String getUnlocalizedName(ItemStack stack) {
		return subBlock(stack).getUnlocalizedName(stack);
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		subBlock(par1ItemStack).addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (Integer i : subBlocks.keySet()) {
			DCls sub = subBlocks.get(i);
			for (int a=0; a<sub.getMetaLength(); a++) {
				if (!sub.isLogicalBlock(a)) par3List.add(new ItemStack(this, 1, i+a));
			}
		}
	}
	
	/* ---------------------------- WorldMethods ---------------------------- */
	
	public void setBlockAt(DCls delegate, World world, int wx, int wy, int wz, int flags) {
		delegate.setBlockAt(world, wx, wy, wz, flags);
	}
	
	/* ---------------------------- BlockMethods ---------------------------- */
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		return subBlock(world, x, y, z).getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		subBlock(world, x, y, z).setBlockBoundsBasedOnState(world, x, y, z);
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 par5Vec3, Vec3 par6Vec3) {
		return subBlock(world, x, y, z).collisionRayTrace(world, x, y, z, par5Vec3, par6Vec3);
	}
	
	public MovingObjectPosition superCollisionRayTrace(World world, int x, int y, int z, Vec3 par5Vec3, Vec3 par6Vec3) {
		return super.collisionRayTrace(world, x, y, z, par5Vec3, par6Vec3);
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).getLightValue(world, x, y, z);
	}
	
	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).getLightOpacity(world, x, y, z);
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).canBeReplacedByLeaves(world, x, y, z);
	}
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).canCreatureSpawn(type, world, x, y, z);
	}
	
	@Override
	public String getUnlocalizedName() {
		// TODO Auto-generated method stub
		return super.getUnlocalizedName();
	}
	
	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int meta) {
		DCls sub = subBlock(world, x, y, z);
		return sub.canSilkHarvest(world, player, x, y, z, meta - sub.metaId);
	}
	
	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).canSustainLeaves(world, x, y, z);
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		return subBlock(world, x, y, z).canSustainPlant(world, x, y, z, direction, plant);
	}
	
	@Override
	public void velocityToAddToEntity(World world, int x, int y, int z, Entity par5Entity, Vec3 par6Vec3) {
		subBlock(world, x, y, z).velocityToAddToEntity(world, x, y, z, par5Entity, par6Vec3);
	}
	
	@Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
		return subBlock(world, x, y, z).isLadder(world, x, y, z, entity);
	}
	
	@Override
	public boolean isFertile(World world, int x, int y, int z) {
		return subBlock(world, x, y, z).isFertile(world, x, y, z);
	}
	
	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).isAir(world, x, y, z);
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		DCls sub = subBlock(world, x, y, z);
		return sub.isFlammable(world, x, y, z, face);
	}
	
	@Override
	public boolean isBurning(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).isBurning(world, x, y, z);
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return subBlock(world, x, y, z).isSideSolid(world, x, y, z, side);
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return subBlock(world, x, y, z).canBlockStay(world, x, y, z);
	}
	
	@Override
	public void fillWithRain(World world, int x, int y, int z) {
		subBlock(world, x, y, z).fillWithRain(world, x, y, z);
	}
	
	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return subBlock(world, x, y, z).canPlaceTorchOnTop(world, x, y, z);
	}
	
	/* ---------------------------- Events ---------------------------- */
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random par5Random) {
		subBlock(world, x, y, z).updateTick(world, x, y, z, par5Random);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		subBlock(world, x, y, z).onBlockAdded(world, x, y, z);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		subBlock(world, x, y, z).onBlockPlacedBy(world, x, y, z, par5EntityLivingBase, par6ItemStack);
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		DCls sub = subBlock(world, x, y, z);
		sub.onBlockPreDestroy(world, x, y, z, meta-sub.metaId);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		DCls sub = subBlock(world, x, y, z);
		sub.breakBlock(world, x, y, z, id, meta-sub.metaId);
	}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		return subBlock(world, x, y, z).removeBlockByPlayer(world, player, x, y, z);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		subBlock(world, x, y, z).onNeighborBlockChange(world, x, y, z, id);
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer par5EntityPlayer) {
		subBlock(world, x, y, z).onBlockClicked(world, x, y, z, par5EntityPlayer);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float par7, float par8, float par9) {
		return subBlock(world, x, y, z).onBlockActivated(world, x, y, z, par5EntityPlayer, side, par7, par8, par9);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity par5Entity) {
		subBlock(world, x, y, z).onEntityCollidedWithBlock(world, x, y, z, par5Entity);
	}
	
	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity par5Entity) {
		subBlock(world, x, y, z).onEntityWalking(world, x, y, z, par5Entity);
	}
	
	/* ---------------------------- Redstone ---------------------------- */
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return subBlock(world, x, y, z).canConnectRedstone(world, x, y, z, side);
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		return subBlock(world, x, y, z).isProvidingStrongPower(world, x, y, z, side);
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return subBlock(world, x, y, z).isProvidingWeakPower(world, x, y, z, side);
	}
	
	/* ---------------------------- TileEntities ---------------------------- */
	
	@Override
	public boolean hasTileEntity(int meta) {
		DCls sub = subBlock(meta);
		return sub.hasTileEntity(meta-sub.metaId);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		DCls sub = subBlock(meta);
		return sub.createTileEntity(world, meta - sub.metaId);
	}
	
	/* ---------------------------- Mining ---------------------------- */
	
	@Override
	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		return subBlock(world, x, y, z).canEntityDestroy(world, x, y, z, entity);
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		DCls sub = subBlock(meta);
		return sub.canHarvestBlock(player, meta-sub.metaId);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side, ItemStack par6ItemStack) {
		return subBlock(world, x, y, z).canPlaceBlockOnSide(world, x, y, z, side, par6ItemStack);
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
		return subBlock(meta).getBlockDropped(world, x, y, z, meta, fortune);
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return subBlock(world, x, y, z).getPickBlock(target, world, x, y, z);
	}
	
	@Override
	public int getExpDrop(World world, int meta, int enchantmentLevel) {
		return subBlock(meta).getExpDrop(world, meta, enchantmentLevel);
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		return subBlock(world, x, y, z).getBlockHardness(world, x, y, z);
	}
	
	/* ---------------------------- ClientSide ---------------------------- */
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getBlockBrightness(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).getBlockBrightness(world, x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		return subBlock(world, x, y, z).getMixedBrightnessForBlock(world, x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		Boolean renderSide = subBlock(world, x, y, z).shouldSideBeRendered(world, x, y, z, side);
		return renderSide != null ? renderSide : super.shouldSideBeRendered(world, x, y, z, side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		DCls sub = subBlock(meta);
		return sub.getIcon(side, meta - sub.metaId);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		return subBlock(world, x, y, z).getBlockTexture(world, x, y, z, side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		DCls sub = subBlock(meta);
		return sub.getRenderColor(meta - sub.metaId);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		for (DCls sub : subBlocks.values()) sub.registerIcons(par1IconRegister);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		DCls sub = subBlock(meta);
		return sub.addBlockDestroyEffects(world, x, y, z, meta - sub.metaId, effectRenderer);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
		return subBlock(world, target.blockX, target.blockY, target.blockZ).addBlockHitEffects(world, target, effectRenderer);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random par5Random) {
		for (DCls sub : subBlocks.values()) sub.randomDisplayTick(world, x, y, z, par5Random);
	}
	
}
