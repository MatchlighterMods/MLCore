package ml.core.gui.controls.tabs;

import net.minecraft.client.gui.FontRenderer;
import ml.core.vec.Rectangle;
import ml.core.vec.Vector2i;

public abstract class TabDynamicLedger extends TabLedger {

	public TabDynamicLedger(ControlTabManager ctm) {
		super(ctm, new Vector2i());
	}

	@Override
	public Vector2i getOpenSize() {
		Rectangle controlRect = calculateControlBox();
		FontRenderer fr = getMC().fontRenderer;
		return new Vector2i(Math.max(controlRect.width + controlRect.xCoord*2, 24 + fr.getStringWidth(getTitle())+8),
				controlRect.yCoord + controlRect.height + (controlRect.yCoord - defaultSize + 8));
	}
}
