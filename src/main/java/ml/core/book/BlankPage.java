package ml.core.book;

import net.minecraft.client.Minecraft;

public class BlankPage extends Page {

	public BlankPage(WindowBook window) {
		super(window);
	}

	@Override
	public void drawPage(Minecraft mc, int x, int y, int w, int h, float partialTick) {}

}
