package ml.core.gui.core.style;

import java.util.ArrayList;
import java.util.List;

import ml.core.gui.core.DefaultControlIconProvider;
import ml.core.gui.core.GuiElement;
import ml.core.texture.CustomTextureMapManager;
import ml.core.texture.IIconProvider;

public class StyleManager {
	
	public static final GuiStyle defaultStyle = new GuiStyle(GuiStyle.defaultDomain, GuiStyle.defaultBasePath, "default");
	private static final List<IIconProvider> iconProviders = new ArrayList<IIconProvider>();
	static {
		registerIconProvider(new DefaultControlIconProvider());
		registerGuiStyle(defaultStyle);
	}
	
	/**
	 * Register an {@link IIconProvider} for custom {@link GuiElement}s. Note: resource domains will be ignored.
	 * @param iip
	 */
	public static void registerIconProvider(IIconProvider iip) {
		iconProviders.add(iip);
	}

	public static void registerGuiStyle(GuiStyle style) {
		CustomTextureMapManager.instance.registerMap(style);
	}
	
	static void registerIconsInStyle(GuiStyle style) { // Got to do it in style ;)
		for (IIconProvider iip : iconProviders) {
			iip.registerIcons(style);
		}
	}
}
