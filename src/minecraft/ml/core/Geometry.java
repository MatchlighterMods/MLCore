package ml.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import ml.core.Geometry.XYPair;
import ml.core.Geometry.rectangle;

public class Geometry {

	public static class rectangle {
		public int xCoord;
		public int yCoord;
		public int width;
		public int height;
		
		public rectangle(int x, int y, int width, int height) {
			this.xCoord = x;
			this.yCoord = y;
			this.width = width;
			this.height = height;
		}
	}

	public static class XYPair {
		public int X;
		public int Y;
		
		public XYPair(int x, int y) {
			this.X = x;
			this.Y = y;
		}
		
		@Override
		public String toString(){
			return "X: " + X + ", Y: " + Y;
		}
	}

	public static Boolean pointInRect(int pntX, int pntY, int rectX, int rectY, int rectW, int rectH){
		return (pntX >= rectX && pntY >= rectY && pntX <= rectX+rectW && pntY <= rectY + rectH);
	}

	public static boolean pointInRect(int pntX, int pntY, Geometry.rectangle r){
		return pointInRect(pntX, pntY, r.xCoord, r.yCoord, r.width, r.height);
	}

	public static XYPair determineSquarestGrid(int elements){
		if (elements == 0)
			return new Geometry.XYPair(0, 0);
		int a = (int)Math.round(Math.sqrt(elements));
		int b = (int)Math.ceil(((float)elements)/a);
		return a>b ? new Geometry.XYPair(a, b) : new Geometry.XYPair(b, a);
	}

	public static XYPair getScaledMouse(){
		Minecraft mc = FMLClientHandler.instance().getClient();
		ScaledResolution var13 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int var14 = var13.getScaledWidth();
		int var15 = var13.getScaledHeight();
		int adjMouseX = Mouse.getX() * var14 / mc.displayWidth;
		int adjMouseY = var15 - Mouse.getY() * var15 / mc.displayHeight - 1;
		
		return new Geometry.XYPair(adjMouseX, adjMouseY);
	}

}
