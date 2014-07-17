package ml.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DelegateBlock {

	protected int metaId;
	protected BlockDelegator<? extends DelegateBlock> parent;
	protected String unlocalizedName;
	
	@SideOnly(Side.CLIENT)
	protected IIcon itemIcon;
	protected String iconLocation;
	
	public DelegateBlock() {}
	
	public BlockDelegator<? extends DelegateBlock> parent() {
		return parent;
	}
	
	public DelegateBlock setIconString(String str) {
		this.iconLocation = str;
		return this;
	}
	
	public DelegateBlock setUnlocalizedName(String str) {
		this.unlocalizedName = str;
		return this;
	}
	
	public final int getMetaId() {
		return metaId;
	}

	public int getMetaLength() {
		return 1;
	}
	
	public int getSubMeta(IBlockAccess w, int x, int y, int z) {
		return getSubMeta(w.getBlockMetadata(x, y, z));
	}
	
	public int getSubMeta(int gmeta) {
		return gmeta - metaId;
	}
	
	public void setBlockAt(World world, int wx, int wy, int wz, int flags) {
		this.setBlockAt(world, wx, wy, wz, 0, flags);
	}
	
	public void setBlockAt(World world, int wx, int wy, int wz, int subMeta, int flags) {
		if (subMeta < getMetaLength()) {
			world.setBlock(wx, wy, wz, parent, metaId+subMeta, flags);
		} else {
			world.setBlock(wx, wy, wz, parent, metaId, flags);
		}
	}
	
	/* ---------------------------- ItemMethods ---------------------------- */
	
	public String getUnlocalizedName(ItemStack stack) {
		return unlocalizedName;
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		
	}
	
	public boolean isLogicalBlock(int subMeta) {
		return false;
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

	public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
		return isOpaqueCube() ? 255 : 0;
	}
	
	public boolean isOpaqueCube() {
		return true;
	}

	public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return isSideSolid(world, x, y, z, ForgeDirection.UP);
	}

	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int subMeta) {
		return false;
	}

	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		return false;
	}

	public void velocityToAddToEntity(World par1World, int x, int y, int z, Entity par5Entity, Vec3 par6Vec3) {}

	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
		return false;
	}

	public boolean isFertile(World world, int x, int y, int z) {
		return false;
	}

	public boolean isAir(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return false;
	}

	public boolean isBurning(IBlockAccess world, int x, int y, int z) {
		return false;
	}
	
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return isOpaqueCube();
	}

	public boolean canBlockStay(World par1World, int x, int y, int z) {
		return true;
	}

	public void fillWithRain(World par1World, int x, int y, int z) {}

	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		return isSideSolid(world, x, y, z, ForgeDirection.UP);
	}

	/* ---------------------------- Events ---------------------------- */

	public void updateTick(World par1World, int x, int y, int z, Random par5Random) {}

	public void onBlockAdded(World par1World, int x, int y, int z) {}

	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {}

	public void onBlockPreDestroy(World par1World, int x, int y, int z, int subMeta) {}

	public void breakBlock(World par1World, int x, int y, int z, int id, int subMeta) {
		if (hasTileEntity(subMeta)) par1World.removeTileEntity(x, y, z);
	}

	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		return world.setBlockToAir(x, y, z);
	}

	public void onNeighborBlockChange(World par1World, int x, int y, int z, int id) {}

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

	public boolean hasTileEntity(int subMeta) {
		return this instanceof ITileEntityProvider;
	}

	public TileEntity createTileEntity(World world, int subMeta) {
		if (hasTileEntity(subMeta))
			return ((ITileEntityProvider)this).createNewTileEntity(world, subMeta);
		return null;
	}

	/* ---------------------------- Mining ---------------------------- */

	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		return true;
	}

	public boolean canHarvestBlock(EntityPlayer player, int subMeta) {
		return ForgeHooks.canHarvestBlock(this.parent(), player, subMeta);
	}

	public boolean canPlaceBlockOnSide(World par1World, int x, int y, int z, int side, ItemStack par6ItemStack) {
		return parent().canPlaceBlockAt(par1World, x, y, z);
	}

	/**
	 * Note that meta is not subMeta in this case.
	 */
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(parent(), 1, meta));
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

	@SideOnly(Side.CLIENT)
	public float getBlockBrightness(IBlockAccess world, int x, int y, int z) {
		return world.getBrightness(x, y, z, getLightValue(world, x, y, z));
	}

	@SideOnly(Side.CLIENT)
	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		return world.getLightBrightnessForSkyBlocks(x, y, z, getLightValue(world, x, y, z));
	}

	@SideOnly(Side.CLIENT)
	public Boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int subMeta) {
		return itemIcon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		return this.getIcon(side, getSubMeta(world, x, y, z));
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(int subMeta) {
		return 16777215;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ireg) {
		this.itemIcon = ireg.registerIcon(iconLocation);
	}

	@SideOnly(Side.CLIENT)
	public boolean addBlockDestroyEffects(World world, int x, int y, int z, int subMeta, EffectRenderer effectRenderer) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random) {}

}
