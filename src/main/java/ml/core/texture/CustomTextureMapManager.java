package ml.core.texture;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomTextureMapManager {
	
	public static final int custom_map_type = 21; 
	
	public static CustomTextureMapManager instance = new CustomTextureMapManager();
	protected final ArrayList<CustomTextureMap> maps = new ArrayList<CustomTextureMap>();
	
	public void registerMap(CustomTextureMap map) {
		if (maps.contains(map))
			throw new RuntimeException("TextureMap type " + map.getTextureType() + " is already registered.");
		
		maps.add(map);
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.renderEngine.loadTextureMap(map.resourceLoc, map);
		try {
			map.loadTexture(mc.getResourceManager());
		} catch (IOException e) {}
	}
	
	@SubscribeEvent
	public void reregisterIcons(TextureStitchEvent.Pre evt) {
		Minecraft mc = Minecraft.getMinecraft();
		if (evt.map.getTextureType()==0) {
			for (CustomTextureMap map : maps) {
				try {
					map.loadTexture(mc.getResourceManager());
				} catch (IOException e) {}
			}
		}
		if (evt.map instanceof CustomTextureMap) {
			((CustomTextureMap)evt.map).reregisterIcons();
		}
	}
}
