package ml.core.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDelegate extends ItemBlock {

	public ItemBlockDelegate(Block par1) {
		super(par1);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		BlockDelegator.findSubBlock(par1ItemStack).addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "tile."+BlockDelegator.findSubBlock(par1ItemStack).getUnlocalizedName(par1ItemStack);
	}
	
}
