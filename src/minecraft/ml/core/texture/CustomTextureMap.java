package ml.core.texture;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class CustomTextureMap extends TextureMap {

	public ResourceLocation resourceLoc;
	
	public CustomTextureMap(int par1, String par2Str, ResourceLocation resLoc) {
		super(par1, par2Str);
		resourceLoc = resLoc;
	}

	/**
	 * Called by MLCore. You code should find whatever is using this TextureMap and re-register the icons into its list.
	 */
	public abstract void reregisterIcons();
}
