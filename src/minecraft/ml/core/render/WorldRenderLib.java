package ml.core.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WorldRenderLib {
	
	private static RenderBlocks renderBlocks = new RenderBlocks();
	private static EntityItem renderEnt;
	
	public static boolean shouldSpreadItems = false;
	public static RenderItem renderItem = new RenderItem() {
		@Override
		public boolean shouldBob() {
			return false;
		}
		@Override
		public boolean shouldSpreadItems() {
			return shouldSpreadItems;
		};
	};
	
	
	static{
		renderItem.setRenderManager(RenderManager.instance);
		renderEnt = new EntityItem(null);
		renderEnt.hoverStart = 0F;
	}
	
	public static void renderItemIntoWorldCenteredAt(ItemStack is, boolean renderIn3D){
		boolean isBlock = is.getItem() instanceof ItemBlock;
		if (!renderIn3D){
			GL11.glPushMatrix();
			GL11.glScalef(0.03125F, 0.03125F, -0.0004F);
			GL11.glTranslatef(8, 8, 0);
			GL11.glRotatef(180F, 0, 0, 1.0F);

			if (!ForgeHooksClient.renderInventoryItem(renderBlocks, RenderManager.instance.renderEngine, is, true, 0, 0F, 0F))
			{
				renderItem.renderItemIntoGUI(null, RenderManager.instance.renderEngine, is, 0, 0);
			}
			GL11.glPopMatrix();
		} else {
			if (isBlock){
				GL11.glScalef(1.2F, 1.2F, 1.2F);
				GL11.glRotatef(90F, 0, 1F, 0);
			} else {
				GL11.glTranslatef(0F, -0.125F, 0F);
			}
			renderEnt.setEntityItemStack(is);
			renderItem.doRenderItem(renderEnt, 0, 0, 0, 0, 0);
		}
	}
}
