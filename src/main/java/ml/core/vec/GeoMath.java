package ml.core.vec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;

public class GeoMath {

	public static Boolean pointInRect(int pntX, int pntY, int rectX, int rectY, int rectW, int rectH){
		return (pntX >= rectX && pntY >= rectY && pntX <= rectX+rectW && pntY <= rectY + rectH);
	}

	public static Vector2i determineSquarestGrid(int elements){
		if (elements == 0)
			return new Vector2i(0, 0);
		int a = (int)Math.round(Math.sqrt(elements));
		int b = (int)Math.ceil(((float)elements)/a);
		return a>b ? new Vector2i(a, b) : new Vector2i(b, a);
	}

	public static Vector2i getScaledMouse(){
		Minecraft mc = FMLClientHandler.instance().getClient();
		ScaledResolution var13 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int var14 = var13.getScaledWidth();
		int var15 = var13.getScaledHeight();
		int adjMouseX = Mouse.getX() * var14 / mc.displayWidth;
		int adjMouseY = var15 - Mouse.getY() * var15 / mc.displayHeight - 1;
		
		return new Vector2i(adjMouseX, adjMouseY);
	}

}
