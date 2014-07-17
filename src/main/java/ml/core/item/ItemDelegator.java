package ml.core.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDelegator <DCls extends DelegateItem> extends Item {

	private BiMap<Integer, DCls> subItems = HashBiMap.create();
	
	protected DCls nullDelegate;
	
	public ItemDelegator(DCls nullDelegate) {
		setHasSubtypes(true);
		this.nullDelegate = nullDelegate;
		this.nullDelegate.parent = this;
	}
	
	public ItemStack createStack(DCls subItem, int size) {
		return new ItemStack(this, size, subItem.getMetaId());
	}

	/* ---------------------------- SubItems ---------------------------- */
	
	public boolean addSubItem(int metaData, DCls sub) {
		if (subItems.containsKey(metaData) || subItems.containsValue(sub)) return false;
		subItems.put(metaData, sub);
		sub.parent = this;
		sub.metaId = metaData;
		return true;
	}
	
	public DCls subItem(int metaData) {
		if (subItems.containsKey(metaData)) return subItems.get(metaData);
		return nullDelegate;
	}
	
	public DCls subItem(ItemStack is) {
		if (is != null && is.getItem() == this) {
			return subItem(is.getItemDamage());
		}
		return nullDelegate;
	}
	
	public static DelegateItem findSubItem(ItemStack is) {
		if (is != null && is.getItem() instanceof ItemDelegator<?>) {
			ItemDelegator<?> idg = (ItemDelegator<?>) is.getItem();
			return idg.subItem(is.getItemDamage());
		}
		return null;
	}
	
	/* ---------------------------- ItemMethods ---------------------------- */
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (DCls sub : subItems.values()) {
			sub.addCreativeStacks(par2CreativeTabs, par3List);
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item."+subItem(par1ItemStack).getUnlocalizedName(par1ItemStack);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		String name = subItem(par1ItemStack).getItemDisplayName(par1ItemStack);
		return name != null ? name : super.getItemDisplayName(par1ItemStack);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return subItem(par1ItemStack).getRarity(par1ItemStack);
	}
	
	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack) {
		return subItem(itemStack).getContainerItemStack(itemStack);
	}
	
	@Override
	public int getDisplayDamage(ItemStack stack) {
		DCls sub = subItem(stack);
		return sub.isDamageable(stack) ? sub.getDamage(stack) : 0;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		DCls sub = subItem(stack);
		return sub.isDamageable(stack) ? sub.getMaxDamage(stack) : 0;
	}
	
	@Override
	public boolean isDamaged(ItemStack stack) {
		DCls sub = subItem(stack);
		return sub.isDamageable(stack) ? sub.getDamage(stack) > 0 : false;
	}
	
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		subItem(par1ItemStack).onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
	}
	
	/* ---------------------------- Events ---------------------------- */
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return subItem(stack).onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return subItem(par1ItemStack).onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return subItem(par1ItemStack).onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}
	
	/* ---------------------------- ClientSide ---------------------------- */
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return subItem(par1ItemStack).getColorFromItemStack(par1ItemStack, par2);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return subItem(par1).getIcon(par1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		return subItem(stack).getIcon(stack, pass);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		return subItem(stack).getIconInHand(stack, renderPass, player, usingItem, useRemaining);
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		return subItem(stack).hasEffect(stack, pass);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		for (DCls sub : subItems.values()) sub.registerIcons(par1IconRegister);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		subItem(par1ItemStack).addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
}
