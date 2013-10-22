package ml.core.gui.core;

import net.minecraft.client.renderer.texture.IconRegister;
import ml.core.texture.IIconProvider;

public class DefaultControlIconProvider implements IIconProvider {

	@Override
	public void registerIcons(IconRegister ireg) {
		ireg.registerIcon("slot");
		ireg.registerIcon("window");

	}

}
