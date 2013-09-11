package ml.core.texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
	
	public static BufferedImage defaultMissingTextureImage = new BufferedImage(64, 64, 2);
	static {
		Graphics graphics = defaultMissingTextureImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 64, 64);
        graphics.setColor(Color.BLACK);
        int i = 10;
        int j = 0;

        while (i < 64)
        {
            String s = j++ % 2 == 0 ? "missing" : "texture";
            graphics.drawString(s, 1, i);
            i += graphics.getFont().getSize();

            if (j % 2 == 0)
            {
                i += 5;
            }
        }

        graphics.dispose();
	}

	/**
	 * Called by MLCore. You code should find whatever is using this TextureMap and re-register the icons into its list.
	 */
	public abstract void reregisterIcons();
}
