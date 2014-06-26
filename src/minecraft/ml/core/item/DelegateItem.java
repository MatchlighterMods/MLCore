package ml.core.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;



public class DelegateItem {

	protected int metaId;
	protected ItemDelegator<? extends DelegateItem> parent;
	
	public DelegateItem() {}
	
	public ItemDelegator<? extends DelegateItem> parent() {
		return parent;
	}
	
	public int getMetaId() {
		return metaId;
	}
	
/* ---------------------------- ItemMethods ---------------------------- */
	
	public String getUnlocalizedName(ItemStack stack) {
		return "";
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
	
	public Icon getIcon(ItemStack stack, int pass) {
		return null;
	}
	
	public Icon getIconInHand(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		return getIcon(stack, renderPass);
	}
	
	public void registerIcons(IconRegister par1IconRegister) {}
	
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {}
}
