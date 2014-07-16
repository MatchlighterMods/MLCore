package ml.core.gui.core.style;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Apply custom styles to Guis without re-writing controls.<br/>
 * Pretty straight-forward: <ol>
 * <li>Create an instance with appropriate domain and basePath</li>
 * <li>Register it as a {@link ResourceManagerReloadListener} with {@link #registerAsReloadListener()}</li>
 * <li>Set it as the style for your Gui</li>
 * </ol>
 * Intended for use as per-style singletons. i.e. Every Gui with the same style will reference the same GuiStyle instance.
 * <br/><br/>
 * <b>To modders adding controls:</b> Resources specific to your control should go in <i>assets/mlcontrols/{{pack_name}}/{{your_name or your_mod_name}}/{{resource}}</i>.
 * This prevents conflicts and keeps things clean.
 * @author Matchlighter
 */
@SideOnly(Side.CLIENT)
public class GuiStyle implements IResourceManagerReloadListener {

	public static final String defaultDomain = "mlcontrols";
	public static final String defaultBasePath = "default";
	
	public static GuiStyle defaultStyle = new GuiStyle(defaultDomain, defaultBasePath, null);
	
	static {
		defaultStyle.registerAsReloadListener();
	}
	
	protected String domain;
	protected String basePath;
	protected GuiStyle parentStyle;
	
	protected Map<String, ResourceLocation> cachedLocations = new HashMap<String, ResourceLocation>();
	protected Properties props = new Properties();
	
	public GuiStyle(String domain, String basePath, GuiStyle parent) {
		this.domain = domain;
		this.basePath = basePath;
		this.parentStyle = parent;
	}
	
	public GuiStyle(String domain, String basePath) {
		new GuiStyle(domain, basePath, defaultStyle);
	}
	
	public ResourceLocation getResourceManual(String name) {
		return new ResourceLocation(domain.toLowerCase(), String.format("%s/%s", basePath, name));
	}
	
	protected ResourceLocation findResource(String feat) {
		ResourceLocation rl = getResourceManual(feat+".png");
		try {
			FMLClientHandler.instance().getClient().getResourceManager().getResource(rl); //Verify existence
			return rl;
		} catch (IOException ioe) {
			if (parentStyle != null)
				return parentStyle.getResource(feat);
			else
				return rl;
		}
	}
	
	/**
	 * Returns either the {@link ResourceLocation} of the Resource in this style, or the {@link ResourceLocation} of the default if this style doesn't define the resource
	 */
	public ResourceLocation getResource(String feat) {
		if (!cachedLocations.containsKey(feat))
			cachedLocations.put(feat, findResource(feat));
		return cachedLocations.get(feat);
	}
	
	public String getProperty(String feat) {
		if (props.containsKey(feat)) {
			return props.getProperty(feat);
		} else if (parentStyle != null) {
			return parentStyle.getProperty(feat);
		} else {
			return null;
		}
	}
	
	private int getRealColorValue(String feat, String ofeat) {
		try {
			String afeat = feat+".color";
			if (props.containsKey(afeat)) {
				String cv = props.getProperty(afeat);
				if (cv.startsWith("@")) { //Link
					cv = cv.substring(1);
					if (cv != ofeat) { //Prevent infinite loops
						return getRealColorValue(cv, ofeat);
					} else {
						return 0;
					}
				} else if (cv.startsWith("#")) { //HEX
					cv = cv.substring(1);
					return (int)Long.parseLong(cv, 16);
				} else {
					return Integer.parseInt(cv); //Decimal
				}
			} else if (parentStyle != null) {
				return parentStyle.getColorValue(feat);
			} else {
				return 0;
			}
		} catch (RuntimeException e) { //Bullet Proofing.
			e.printStackTrace();
			return 0;
		}
	}
	
	public int getColorValue(String feat) {
		return getRealColorValue(feat, feat);
	}
	
	/**
	 * Should always be called when Textures are reloaded.
	 */
	public void clearCache() {
		cachedLocations.clear();
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourcemanager) {
		clearCache();
		
		//Load Properties and Colors
		IResourceManager rm = FMLClientHandler.instance().getClient().getResourceManager();
		props.clear();
		try {
			List<IResource> robjs = rm.getAllResources(getResourceManual("properties.txt")); //TODO 1.7.2: Make sure these load in the correct order
			for (IResource res : robjs) {
				props.load(res.getInputStream());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerAsReloadListener() {
		((IReloadableResourceManager)FMLClientHandler.instance().getClient().getResourceManager()).registerReloadListener(this);
	}

}
