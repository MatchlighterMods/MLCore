package ml.core.gui.core.style;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.util.ResourceLocation;

/**
 * A simple sub-class of {@link GuiStyle}.
 * This class allows you to programmatically overload certain ResourceLocations or colors.
 * Useful for one-off or dynamic styles.
 * 
 * @author Matchlighter
 */
@SideOnly(Side.CLIENT)
public class GuiStyleManip extends GuiStyle {

	public Map<String, String> resOverrides = new HashMap<String, String>();
	
	public GuiStyleManip(GuiStyle parent) {
		super(parent.domain, parent.basePath, parent);
	}
	
	public void addResourceOverride(String feat, String npath) {
		resOverrides.put(feat, npath);
	}

	public void addPropertyOverride(String ident, String color) {
		this.props.put(ident, color);
	}
	
	/**
	 * @param color Is expected to be a link to another color, a hex string prefixed with # (#RRGGBB), or an integer
	 */
	public void addColorOverride(String ident, String color) {
		addPropertyOverride(ident+".color", color);
	}
	
	@Override
	protected ResourceLocation findResource(String feat) {
		if (resOverrides.containsKey(feat))
			return new ResourceLocation(resOverrides.get(feat)+".png");
		return parentStyle.findResource(feat);
	}
	
	/**
	 * You don't need to register {@link GuiStyleManip} as a ReloadListener.
	 */
	@Override
	public void registerAsReloadListener() {}
}
