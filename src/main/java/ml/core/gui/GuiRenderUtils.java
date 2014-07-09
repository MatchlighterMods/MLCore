package ml.core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRenderUtils {
	public static RenderItem renderItem = new RenderItem();
	public static int zLevel = 0;

	public static void drawTexturedModelRect(int x, int y, double u, double v, double uM, double vM, int w, int h) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)zLevel, u, vM);
		tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)zLevel, uM, vM);
		tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)zLevel, uM, v);
		tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)zLevel, u, v);
		tessellator.draw();
	}
	
	public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, int tw, int th) {
		drawTexturedModelRect(x, y, (float)u/(float)tw, (float)v/(float)th, (float)(u+width)/(float)tw, (float)(v+height)/(float)th, width, height);
	}
	
	public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		drawTexturedModalRect(x, y, u, v, width, height, 256, 256);
	}

	public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float var7 = (float)(par5 >> 24 & 255) / 255.0F;
		float var8 = (float)(par5 >> 16 & 255) / 255.0F;
		float var9 = (float)(par5 >> 8 & 255) / 255.0F;
		float var10 = (float)(par5 & 255) / 255.0F;
		float var11 = (float)(par6 >> 24 & 255) / 255.0F;
		float var12 = (float)(par6 >> 16 & 255) / 255.0F;
		float var13 = (float)(par6 >> 8 & 255) / 255.0F;
		float var14 = (float)(par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator var15 = Tessellator.instance;
		var15.startDrawingQuads();
		var15.setColorRGBA_F(var8, var9, var10, var7);
		var15.addVertex((double)par3, (double)par2, (double)zLevel);
		var15.addVertex((double)par1, (double)par2, (double)zLevel);
		var15.setColorRGBA_F(var12, var13, var14, var11);
		var15.addVertex((double)par1, (double)par4, (double)zLevel);
		var15.addVertex((double)par3, (double)par4, (double)zLevel);
		var15.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawStackAt(Minecraft mc, int x, int y, ItemStack is){
		GL11.glEnable(GL11.GL_LIGHTING);
		renderItem.zLevel = 100F;
		renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, is, x, y);
		renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, is, x, y);
		renderItem.zLevel = 0F;
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	public static void drawSpecialStackAt(Minecraft mc, int x, int y, ItemStack is, String str){
		GL11.glEnable(GL11.GL_LIGHTING);
		ItemStack tis = is.copy();
		tis.stackSize = 1;
		renderItem.zLevel = 100F;
		renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, tis, x, y);
		renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, tis, x, y);
		renderItem.zLevel = 0F;

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		mc.fontRenderer.drawStringWithShadow(str, x + 19 - 2 - mc.fontRenderer.getStringWidth(str), y + 6 + 3, 16777215);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glDisable(GL11.GL_LIGHTING);
	}

	public static void drawSlicedRect(int x, int y, int w, int h, int uBase, int vBase, int uW, int uH, int tBord, int rBord, int bBord, int lBord, int tw, int th){
		drawTexturedModalRect(x, y, uBase, vBase, w-rBord, h-bBord, tw, th);
		drawTexturedModalRect(x+lBord, y, uBase+uW-(w-lBord), vBase, w-lBord, h-bBord, tw, th);
		drawTexturedModalRect(x+lBord, y+tBord, uBase+uW-(w-lBord), vBase+uH-(h-tBord), w-lBord, h-tBord, tw, th);
		drawTexturedModalRect(x, y+tBord, uBase, vBase+uH-(h-tBord), w-rBord, h-tBord, tw, th);
	}
	
	public static void drawSlicedRect(int x, int y, int w, int h, int uBase, int vBase, int uW, int uH, int tBord, int rBord, int bBord, int lBord){
		drawSlicedRect(x, y, w, h, uBase, vBase, uW, uH, tBord, rBord, bBord, lBord, 256, 256);
	}

	public static void drawSlicedRect(int x, int y, int w, int h, int uBase, int vBase, int uW, int uH, int bord){
		drawSlicedRect(x, y, w, h, uBase, vBase, uW, uH, bord, bord, bord, bord);
	}

	public static void drawTexturedModelRectFromIcon(int par1, int par2, IIcon par3Icon, int par4, int par5) {
		drawTexturedModelRect(par1, par2, par3Icon.getMinU(), par3Icon.getMinV(), par3Icon.getMaxU(), par3Icon.getMaxV(), par4, par5);
	}
	
	public static void drawSlicedRectFromIcon(int x, int y, int w, int h, IIcon i, int tBord, int rBord, int bBord, int lBord) {
		double dtBord = (double)tBord / (double)i.getIconHeight();
		double dbBord = (double)bBord / (double)i.getIconHeight();
		double drBord = (double)tBord / (double)i.getIconWidth();
		double dlBord = (double)lBord / (double)i.getIconWidth();
		
		drawTexturedModelRect(x, y, i.getMinU(), i.getMinV(), i.getMaxU()-drBord, i.getMaxV()-dbBord, w-rBord, h-bBord);
		drawTexturedModelRect(x+lBord, y, i.getMinU()+dlBord, i.getMinV(), i.getMaxU(), i.getMaxV()-dbBord, w-lBord, h-bBord);
		drawTexturedModelRect(x+lBord, y+tBord, i.getMinU()+dlBord, i.getMinV()+dtBord, i.getMaxU(), i.getMaxV(), w-lBord, h-tBord);
		drawTexturedModelRect(x, y+tBord, i.getMinU(), i.getMinV()+dtBord, i.getMaxU()-drBord, i.getMaxV(), w-rBord, h-tBord);
	}
	
}
