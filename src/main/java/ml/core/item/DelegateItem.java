package ml.core.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class DelegateItem {

	protected int metaId;
	protected ItemDelegator<? extends DelegateItem> parent;
	protected String unlocalizedName;
	
	@SideOnly(Side.CLIENT)
	protected IIcon itemIcon;
	protected String iconLocation;
	
	public DelegateItem() {}
	
	public DelegateItem setIconString(String str) {
		this.iconLocation = str;
		return this;
	}
	
	public DelegateItem setUnlocalizedName(String str) {
		this.unlocalizedName = str;
		return this;
	}
	
	public ItemDelegator<? extends DelegateItem> parent() {
		return parent;
	}
	
	public int getMetaId() {
		return metaId;
	}
	
	public ItemStack createStack(int size) {
		return new ItemStack(parent(), size, getMetaId());
	}
	
	/* ---------------------------- ItemMethods ---------------------------- */
	
	@SideOnly(Side.CLIENT)
	public void addCreativeStacks(CreativeTabs ctab, List stackList) {
		stackList.add(new ItemStack(parent(), 1, getMetaId()));
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		return unlocalizedName;
	}
	
	/**
	 * Return null for default implementation
	 */
	public String getItemDisplayName(ItemStack stack) {
		return null;
	}
	
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.common;
	}
	
	public ItemStack getContainerItemStack(ItemStack itemStack) {
		return null;
	}
	
	public int getDamage(ItemStack stack) {
		return 0;
	}
	
	public int getMaxDamage(ItemStack stack) {
		return 0;
	}
	
	public boolean isDamageable(ItemStack stack) {
		return false;
	}
	
	public void onUpdate(ItemStack stack, World par2World, Entity par3Entity, int par4, boolean par5) {}
	
	/* ---------------------------- Events ---------------------------- */
	
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return false;
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer) {
		return stack;
	}
	
	/* ---------------------------- ClientSide ---------------------------- */
	
	public int getColorFromItemStack(ItemStack stack, int par2) {
		return 16777215;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int meta) {
		return itemIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIcon(stack.getItemDamage());
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIconInHand(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		return getIcon(stack, renderPass);
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ireg) {
		this.itemIcon = ireg.registerIcon(iconLocation);
	}
	
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {}
}
