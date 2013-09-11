package ml.core.texture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

public class TextureSheet {

	public class TextureSprite extends TextureAtlasSprite {
		protected String textureName;
		protected int index;
		
		protected TextureSprite(String par1, int tindex) {
			super(par1);
			textureName = par1;
			index = tindex;
		}
		
		@Override
		public boolean load(ResourceManager manager, ResourceLocation location)
				throws IOException {
			int offX = index%tilesX * swidth;
			int offY = index/tilesY * sheight;
			
			BufferedImage spri = masterImg.getSubimage(offX, offY, swidth, sheight);
			ByteArrayOutputStream ot = new ByteArrayOutputStream();
			ImageIO.write(spri, "png", ot);
			SimpleResource res = new SimpleResource(location, new ByteArrayInputStream(ot.toByteArray()), null, null);
			
			super.func_130100_a(res);
			return true;
		}
		
	}
	
	protected TextureSprite[] sprites;
	protected List<TextureSprite> unreggedSprites = new ArrayList<TextureSprite>();
	
	protected BufferedImage masterImg;
	
	protected String texFile;
	protected int tilesX;
	protected int tilesY;
	protected int swidth;
	protected int sheight;
	
	public TextureSheet(String fl, int tilesX, int tilesY) {
		texFile = fl;
		this.tilesX = tilesX;
		this.tilesY = tilesY;
		sprites = new TextureSprite[tilesX*tilesY];
	}
	
	public void registerIcons(IconRegister reg) {
		TextureMap tmap = (TextureMap)reg;
		if (TextureUtils.shouldReloadTexture(tmap, texFile)) {
			loadMasterImg();
			for (int i=0; i<sprites.length; i++)
				if (sprites[i] != null)
					tmap.setTextureEntry(sprites[i].getIconName(), sprites[i]);
		} else {
			for (TextureSprite ts : unreggedSprites)
				tmap.setTextureEntry(ts.getIconName(), ts);
		}
		unreggedSprites.clear();
	}
	
	protected void loadMasterImg() {
		masterImg = TextureUtils.loadBufferedImage(texFile);
		swidth = masterImg.getWidth()/tilesX;
		sheight = masterImg.getHeight()/tilesY;
	}
	
	public TextureSprite initSprite(int i) {
		if (sprites[i] == null) {
			sprites[i] = new TextureSprite(texFile+"-"+i, i);
			unreggedSprites.add(sprites[i]);
		}
		return sprites[i];
	}
	
	public Icon getSprite(int i) {
		return sprites[i];
	}
}
