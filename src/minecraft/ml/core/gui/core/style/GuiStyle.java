package ml.core.gui.core.style;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.minecraft.client.resources.ReloadableResourceManager;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.ResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * Apply custom styles to Guis without re-writing controls.<br/>
 * Pretty straight-forward: <ol>
 * <li>Create an instance with appropriate domain and basePath</li>
 * <li>Register it as a {@link ResourceManagerReloadListener} with {@link #registerAsReloadListener()}</li>
 * <li>Set it as the style for your Gui</li>
 * </ol>
 * Intended for use as per-style singletons. i.e. Every Gui with the same style will reference the same GuiStyle instance. 
 * @author Matchlighter
 */
public class GuiStyle implements ResourceManagerReloadListener {

	public static final String defaultDomain = "MLCore";
	public static final String defaultBasePath = "textures/gui";
	
	public static GuiStyle defaultStyle = new GuiStyle(defaultDomain, defaultBasePath, null);
	
	static {
		defaultStyle.registerAsReloadListener();
	}
	
	protected String domain;
	protected String basePath;
	protected GuiStyle parentStyle;
	
	protected Map<String, ResourceLocation> cachedLocations = new HashMap<String, ResourceLocation>();
	protected Properties pcolors = new Properties();
	
	public GuiStyle(String domain, String basePath, GuiStyle parent) {
		this.domain = domain;
		this.basePath = basePath;
		this.parentStyle = parent;
	}
	
	public GuiStyle(String domain, String basePath) {
		new GuiStyle(domain, basePath, defaultStyle);
	}
	
	public ResourceLocation getResourceManual(String name) {
		return new ResourceLocation(defaultDomain.toLowerCase(), String.format("%s/%s", basePath, name));
	}
	
	protected ResourceLocation findResource(String feat) {
		ResourceLocation rl = new ResourceLocation(domain.toLowerCase(), String.format("%s/%s.png", basePath, feat));
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
	
	private int getRealColorValue(String feat, String ofeat) {
		try {
			if (pcolors.containsKey(feat)) {
				String cv = pcolors.getProperty(feat);
				if (cv.startsWith("@")) {
					cv = cv.substring(1);
					if (cv != ofeat) { //Prevent infinite loops
						return getRealColorValue(cv, ofeat);
					} else {
						return 0;
					}
				} else {
					return Integer.parseInt(cv, 16);
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
	public void onResourceManagerReload(ResourceManager resourcemanager) {
		clearCache();
		
		//Load Colors
		ResourceManager rm = FMLClientHandler.instance().getClient().getResourceManager();
		pcolors.clear();
		try {
			List<Resource> robjs = rm.getAllResources(getResourceManual("colors.txt")); //TODO 1.7.2: Make sure these load in the correct order
			for (Resource res : robjs) {
				pcolors.load(res.getInputStream());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerAsReloadListener() {
		((ReloadableResourceManager)FMLClientHandler.instance().getClient().getResourceManager()).registerReloadListener(this);
	}

}
