package ml.core.world;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WorldRenderUtils {
	
	private static RenderBlocks renderBlocks = new RenderBlocks();
	private static EntityItem renderEnt = new EntityItem(null);
	
	private static boolean shouldSpreadItems = false;
	private static RenderItem renderItem = new RenderItem() {
		@Override
		public boolean shouldBob() {
			return false;
		}
		@Override
		public boolean shouldSpreadItems() {
			return shouldSpreadItems;
		};
	};
	
	
	static {
		renderItem.setRenderManager(RenderManager.instance);
		renderEnt.hoverStart = 0F;
	}
	
	public static void renderItemIntoWorldCenteredAt(ItemStack is, boolean renderIn3D, boolean spreadItems) {
		shouldSpreadItems = spreadItems;
		if (is != null) {
			boolean isBlock = is.getItem() instanceof ItemBlock;
			if (!renderIn3D){
				GL11.glPushMatrix();
				GL11.glScalef(0.03125F, 0.03125F, -0.0004F);
				GL11.glTranslatef(8, 8, 0);
				GL11.glRotatef(180F, 0, 0, 1.0F);
	
				if (!ForgeHooksClient.renderInventoryItem(renderBlocks, RenderManager.instance.renderEngine, is, true, 0, 0F, 0F)) {
					renderItem.renderItemIntoGUI(null, RenderManager.instance.renderEngine, is, 0, 0);
				}
				GL11.glPopMatrix();
			} else {
				IItemRenderer cstm = MinecraftForgeClient.getItemRenderer(is, ItemRenderType.ENTITY);
				boolean renderAs3DBlock = cstm != null && cstm.shouldUseRenderHelper(ItemRenderType.ENTITY, is, ItemRendererHelper.BLOCK_3D);
				if (isBlock || renderAs3DBlock){
					if (renderAs3DBlock || RenderBlocks.renderItemIn3d(((ItemBlock)is.getItem()).field_150939_a.getRenderType())) {
						GL11.glScalef(1.5F, 1.5F, 1.5F);
						GL11.glRotatef(90F, 0, 1F, 0);
					} else {
						GL11.glTranslatef(0F, -0.125F, 0F);
					}
				} else {
					GL11.glTranslatef(0F, -0.125F, 0F);
				}
				//This may happen when used from within an ItemRenderer. It should fix itself after one render loop.
				if (RenderManager.instance.renderEngine != null) {
					renderEnt.setEntityItemStack(is);
					renderItem.doRender(renderEnt, 0, 0, 0, 0, 0);
				}
			}
		}
	}
	
	public static void renderItemIntoWorldCenteredAt(ItemStack is, boolean renderIn3D) {
		renderItemIntoWorldCenteredAt(is, renderIn3D, false);
	}
	
}
