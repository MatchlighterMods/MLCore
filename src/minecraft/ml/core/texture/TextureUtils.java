package ml.core.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.client.texturepacks.ITexturePack;

import org.lwjgl.opengl.GL11;

public class TextureUtils {

	public static Texture createDefaultTexture(String nm, int w, int h) {
		return new Texture(nm, 2, w, h, GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, null);
	}
	
	public static Texture loadTextureFromFile(String file) {
		BufferedImage bfi = loadBufferedImage(file);
		return new Texture(file, 2, bfi.getWidth(), bfi.getHeight(), GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, bfi);
	}
	
	public static Texture loadTextureFromImage(String name, BufferedImage bfi) {
		return new Texture(name, 2, bfi.getWidth(), bfi.getHeight(), GL11.GL_CLAMP, GL11.GL_RGBA, GL11.GL_NEAREST, GL11.GL_NEAREST, bfi);
	}
	
	public static BufferedImage loadBufferedImage(String file) {
		try {
			InputStream is = Minecraft.getMinecraft().renderEngine.texturePack.getSelectedTexturePack().getResourceAsStream(file);
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
		return mp.setTextureEntry(texFile, new TextureStitched(texFile) {
			@Override
			public boolean loadTexture(TextureManager manager, ITexturePack texturepack, String name, String fileName, BufferedImage image, ArrayList textures) {
				return false;
			}
		}); 
	}
}
