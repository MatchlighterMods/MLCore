package ml.core.book;

import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class TitlePage extends Page {

	protected String title;
	protected String subtitle;
	
	public TitlePage(WindowBook window, String title, String subtitle) {
		super(window);
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {
		List<String> lines = mc.fontRendererObj.listFormattedStringToWidth(title, w);
		y = y + (h-lines.size()*mc.fontRendererObj.FONT_HEIGHT)/2;
		for (String ln : lines) {
			mc.fontRendererObj.drawString(ln, x+(w-mc.fontRendererObj.getStringWidth(ln))/2, y, 0x000000);
			y += mc.fontRendererObj.FONT_HEIGHT;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x + w/2, y , 0);
		GL11.glScalef(0.5F, 0.5F, 1);
		mc.fontRendererObj.drawString(subtitle, -mc.fontRendererObj.getStringWidth(subtitle)/2, 0, 0x000000);
		GL11.glPopMatrix();
	}

}
