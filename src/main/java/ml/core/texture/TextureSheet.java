package ml.core.texture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A class that can load icons off of a TextureSheet. Particularly useful for connected textures.
 * @author Matchlighter
 */
@SideOnly(Side.CLIENT)
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
		public boolean load(IResourceManager manager, ResourceLocation location) {
			int offX = index%tilesX * swidth;
			int offY = index/tilesY * sheight;
			
			BufferedImage spri = masterImg.getSubimage(offX, offY, swidth, sheight);
			ByteArrayOutputStream ot = new ByteArrayOutputStream();
			ImageIO.write(spri, "png", ot);
			SimpleResource res = new SimpleResource(location, new ByteArrayInputStream(ot.toByteArray()), null, null);
			
			super.loadSprite(res);
			return true;
		}
		
		@Override
		public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
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
	
	/**
	 * Call in your code (e.g. on {@link Block#registerIcons(IconRegister)})
	 */
	public void registerIcons(IIconRegister reg) {
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
	
	public void initSpriteList(int[] indices) {
		for (int i=0; i<indices.length; i++) {
			initSprite(i);
		}
	}
	
	public IIcon getSprite(int i) {
		return sprites[i];
	}
}
