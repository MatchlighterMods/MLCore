package ml.core.texture.maps;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;

import ml.core.texture.CustomTextureMap;
import ml.core.texture.CustomTextureMapManager;

public class BasicCustomTextureMap extends CustomTextureMap {

	public static final BasicCustomTextureMap GUI = new BasicCustomTextureMap(CustomTextureMapManager.getNextMapId(), "gui", "textures/gui/", defaultMissingTextureImage);
	
	
	public List<IIconProvider> providers = new ArrayList<IIconProvider>();
	
	public void addProvider(IIconProvider pvdr) {
		providers.add(pvdr);
	}
	
	public BasicCustomTextureMap(int par1, String par2, String par3Str, BufferedImage par4BufferedImage) {
		super(par1, par2, par3Str, par4BufferedImage);
		
		CustomTextureMapManager.instance.registerMap(this);
	}

	@Override
	public void refreshTextures() {
		super.refreshTextures();
	}
	
	@Override
	public void reregisterIcons() {
		for (IIconProvider IIP : providers) {
			IIP.registerIcons(this);
		}
	}

	public interface IIconProvider {
		public void registerIcons(IconRegister ireg);
	}
	
}
