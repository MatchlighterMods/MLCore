package ml.core.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class TextureUtils {

//	public static Texture createDefaultTexture(String nm, int w, int h) {
//		return new Texture(nm, 2, w, h, GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, null);
//	}
//	
//	public static Texture loadTextureFromFile(String file) {
//		BufferedImage bfi = loadBufferedImage(file);
//		return new Texture(file, 2, bfi.getWidth(), bfi.getHeight(), GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, bfi);
//	}
//	
//	public static Texture loadTextureFromImage(String name, BufferedImage bfi) {
//		return new Texture(name, 2, bfi.getWidth(), bfi.getHeight(), GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, bfi);
//	}
	
	public static BufferedImage loadBufferedImage(String fl) {
		return loadBufferedImage(new ResourceLocation(fl));
	}
	
	public static BufferedImage loadBufferedImage(ResourceLocation resLoc) {
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(resLoc).getInputStream();
			if (is != null) {
				BufferedImage bfi = loadBufferedImage(is);
				return bfi;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage loadBufferedImage(InputStream is) throws IOException {
		BufferedImage bfi = ImageIO.read(is);
		is.close();
		return bfi;
	}
	
	public static boolean shouldReloadTexture(TextureMap mp, String texFile) {
		return mp.setTextureEntry(texFile, new TextureAtlasSprite(texFile) {
			@Override
			public boolean load(IResourceManager manager, ResourceLocation location) {
				return false;
			}
		}); 
	}
}
