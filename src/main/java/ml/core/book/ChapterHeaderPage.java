package ml.core.book;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

public class ChapterHeaderPage extends Page {

	public List<String> title;
	public List<String> text = new ArrayList<String>();
	
	public ChapterHeaderPage(WindowBook window, String title, List<String> text) {
		super(window);

		this.title = getFontRenderer().listFormattedStringToWidth(title, window.getPageWidth());
		
		int h = (int)(window.pageHeight * 0.25F) + (this.title.size()*getFontRenderer().FONT_HEIGHT)/2 + getFontRenderer().FONT_HEIGHT;
		
		while ((h+=getFontRenderer().FONT_HEIGHT) < window.getPageHeight() && text.size()>0) {
			this.text.add(text.remove(0));
		}
	}

	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {
		
		y = y + (int)(h*0.25F) - (title.size()*mc.fontRendererObj.FONT_HEIGHT)/2;
		
		for (String ln : title) {
			mc.fontRendererObj.drawString(ln, x+(w-mc.fontRendererObj.getStringWidth(ln))/2, y, 0x000000);
			y += mc.fontRendererObj.FONT_HEIGHT;
		}
		
		y += mc.fontRendererObj.FONT_HEIGHT;
		for (String ln : text) {
			mc.fontRendererObj.drawString(ln, x, y, 0x000000);
			y += mc.fontRendererObj.FONT_HEIGHT;
		}
	}

}
