package ml.core.gui.core.style;

import java.io.IOException;

import ml.core.texture.CustomTextureMap;
import ml.core.texture.CustomTextureMapManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

public class GuiStyle extends CustomTextureMap {

	protected String domain;
	protected String basePath;
	public static final String defaultDomain = "MLCore";
	public static final String defaultBasePath = "textures/gui";
	
	public GuiStyle(String domain, String basePath, String styleName) {
		super(CustomTextureMapManager.getNextMapId(), "", new ResourceLocation("MLCore:textures/atlas/styles/"+styleName+".png"));
		this.domain = domain;
		this.basePath = basePath;
	}
	
	@Override
	public Icon registerIcon(String par1Str) {
		super.setTextureEntry(par1Str, new TextureFallback(par1Str));
		return super.getTextureExtry(par1Str);
	}
	
	@Override
	public void reregisterIcons() {
		StyleManager.registerIconsInStyle(this);
	}
	
	public Icon getIconFor(String feature) {
		return getTextureExtry(feature);
	}
	
	protected class TextureFallback extends TextureAtlasSprite {
		public TextureFallback(String name) {
			super(name);
		}
		
		@Override
		public boolean load(ResourceManager manager, ResourceLocation location) throws IOException {
			try {
				return super.load(manager, new ResourceLocation(domain.toLowerCase(), String.format("%s%s", basePath, location.getResourcePath())));
			} catch (IOException ex) {
				return super.load(manager, new ResourceLocation(defaultDomain.toLowerCase(), String.format("%s%s", defaultBasePath, location.getResourcePath())));
			}
		}
	}
}
