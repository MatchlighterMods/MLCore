package ml.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

public class DelegateBlock {

	protected int metaId;
	protected BlockDelegator<? extends DelegateBlock> parent;

	public DelegateBlock() {}
	
	public BlockDelegator<? extends DelegateBlock> parent() {
		return parent;
	}
	
	public int getMetaId() {
		return metaId;
	}

	public void setBlockAt(World world, int wx, int wy, int wz, int flags) {
		world.setBlock(wx, wy, wz, parent.blockID, metaId, flags);
	}
	
	/* ---------------------------- ItemMethods ---------------------------- */
	
	public String getUnlocalizedName(ItemStack stack) {
		return "";
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		
	}
	
	/* ---------------------------- BlockMethods ---------------------------- */

	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		return getBlockHardness(world, x, y, z);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		parent().setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	public MovingObjectPosition collisionRayTrace(World par1World, int x, int y, int z, Vec3 par5Vec3, Vec3 par6Vec3) {
		return parent().superCollisionRayTrace(par1World, x, y, z, par5Vec3, par6Vec3);
	}

	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return 0;
	}

	public int getLightOpacity(World world, int x, int y, int z) {
		return isOpaqueCube() ? 255 : 0;
	}
	
	public boolean isOpaqueCube() {
		return true;
	}

	public boolean canBeReplacedByLeaves(World world, int x, int y, int z) {
		return false;
	}

	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
		return isBlockSolidOnSide(world, x, y, z, ForgeDirection.UP);
	}

	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		return false;
	}

	public boolean canSustainLeaves(World world, int x, int y, int z) {
		return false;
	}

	public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		return false;
	}

	public void velocityToAddToEntity(World par1World, int x, int y, int z, Entity par5Entity, Vec3 par6Vec3) {}

	public boolean isLadder(World world, int x, int y, int z, EntityLivingBase entity) {
		return false;
	}

	public boolean isFertile(World world, int x, int y, int z) {
		return false;
	}

	public boolean isAirBlock(World world, int x, int y, int z) {
		return false;
	}

	public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
		return false;
	}

	public boolean isBlockBurning(World world, int x, int y, int z) {
		return false;
	}
	
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return isOpaqueCube();
	}

	public boolean canBlockStay(World par1World, int x, int y, int z) {
		return true;
	}

	public void fillWithRain(World par1World, int x, int y, int z) {}

	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return isBlockSolidOnSide(world, x, y, z, ForgeDirection.UP);
	}

	/* ---------------------------- Events ---------------------------- */

	public void updateTick(World par1World, int x, int y, int z, Random par5Random) {}

	public void onBlockAdded(World par1World, int x, int y, int z) {}

	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {}

	public void onBlockPreDestroy(World par1World, int x, int y, int z, int meta) {}

	public void breakBlock(World par1World, int x, int y, int z, int id, int meta) {
		if (hasTileEntity(meta)) par1World.removeBlockTileEntity(x, y, z);
	}

	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		return world.setBlockToAir(x, y, z);
	}

	public void onNeighborBlockChange(World par1World, int x, int y, int z, int par5) {}

	public void onBlockClicked(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer) {}

	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitx, float hity, float hitz) {
		return false;
	}

	public void onEntityCollidedWithBlock(World par1World, int x, int y, int z, Entity par5Entity) {}

	public void onEntityWalking(World par1World, int x, int y, int z, Entity par5Entity) {}

	/* ---------------------------- Redstone ---------------------------- */

	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return false;
	}

	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int par5) {
		return 0;
	}

	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int par5) {
		return 0;
	}

	/* ---------------------------- TileEntities ---------------------------- */

	public boolean hasTileEntity(int metadata) {
		return this instanceof ITileEntityProvider;
	}

	public TileEntity createTileEntity(World world, int metadata) {
		if (hasTileEntity(metadata))
			return ((ITileEntityProvider)this).createNewTileEntity(world);
		return null;
	}

	/* ---------------------------- Mining ---------------------------- */

	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		return true;
	}

	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		return ForgeHooks.canHarvestBlock(this.parent(), player, meta);
	}

	public boolean canPlaceBlockOnSide(World par1World, int x, int y, int z, int par5, ItemStack par6ItemStack) {
		return parent().canPlaceBlockAt(par1World, x, y, z);
	}

	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(parent(), 1, metadata));
		return ret;
	}

	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(parent(), 1, world.getBlockMetadata(x, y, z));
	}

	public int getExpDrop(World world, int data, int enchantmentLevel) {
		return 0;
	}

	public float getBlockHardness(World par1World, int x, int y, int z) {
		return 1.5F;
	}

	/* ---------------------------- ClientSide ---------------------------- */

	public float getBlockBrightness(IBlockAccess world, int x, int y, int z) {
		return world.getBrightness(x, y, z, getLightValue(world, x, y, z));
	}

	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		return world.getLightBrightnessForSkyBlocks(x, y, z, getLightValue(world, x, y, z));
	}

	public Boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return null;
	}

	public Icon getIcon(int side, int meta) {return null;}

	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}

	public int getRenderColor(int meta) {
		return 16777215;
	}

	public void registerIcons(IconRegister par1IconRegister) {};

	public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		return false;
	}

	public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
		return false;
	}

	public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random) {}

}
