package ml.core.gui.core.style;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.ReloadableResourceManager;
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
 * @author Matchlighter
 */
public class GuiStyle implements ResourceManagerReloadListener {

	public static final String defaultDomain = "MLCore";
	public static final String defaultBasePath = "textures/gui";
	
	public static GuiStyle defaultStyle = new GuiStyle(defaultDomain, defaultBasePath) {
		@Override
		public ResourceLocation findResource(String feat) {
			return new ResourceLocation(defaultDomain.toLowerCase(), String.format("%s/%s.png", defaultBasePath, feat));
		}
	};
	static {
		defaultStyle.registerAsReloadListener();
	}
	
	protected String domain;
	protected String basePath;
	
	protected Map<String, ResourceLocation> cache = new HashMap<String, ResourceLocation>();
	
	public GuiStyle(String domain, String basePath) {
		this.domain = domain;
		this.basePath = basePath;
	}
	
	protected ResourceLocation findResource(String feat) {
		try {
			ResourceLocation rl = new ResourceLocation(domain.toLowerCase(), String.format("%s/%s.png", basePath, feat));
			FMLClientHandler.instance().getClient().getResourceManager().getResource(rl);
			return rl;
		} catch (IOException ioe) {
			return defaultStyle.getResource(feat);
		}
	}
	
	/**
	 * Returns either the {@link ResourceLocation} of the Resource in this style, or the {@link ResourceLocation} of the default if this style doesn't define the resource
	 */
	public ResourceLocation getResource(String feat) {
		if (!cache.containsKey(feat))
			cache.put(feat, findResource(feat));
		return cache.get(feat);
	}
	
	/**
	 * Should always be called when Textures are reloaded.
	 */
	public void clearCache() {
		cache.clear();
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourcemanager) {
		clearCache();
	}
	
	public void registerAsReloadListener() {
		((ReloadableResourceManager)FMLClientHandler.instance().getClient().getResourceManager()).registerReloadListener(this);
	}

}
