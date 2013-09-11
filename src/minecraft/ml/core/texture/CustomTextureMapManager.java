package ml.core.texture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomTextureMapManager {
	
	private static int lastId = 1;
	public static int getNextMapId() {
		return ++lastId;
	}
	
	public static CustomTextureMapManager instance = new CustomTextureMapManager();

	protected final Map<Integer, CustomTextureMap> maps = new HashMap<Integer, CustomTextureMap>();
	
	public void registerMap(CustomTextureMap map) {
		if (map.textureType < 2 || maps.containsKey(map.textureType))
			throw new RuntimeException("TextureMap type " + map.textureType + " is already registered.");
		
		maps.put(map.textureType, map);
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.renderEngine.func_130088_a(map.resourceLoc, map);
		try {
			map.func_110551_a(mc.func_110442_L());
		} catch (IOException e) {}
	}
	
	@ForgeSubscribe
	public void reregisterIcons(TextureStitchEvent.Pre evt) {
		Minecraft mc = Minecraft.getMinecraft();
		if (evt.map.textureType==0) {
			for (CustomTextureMap map : maps.values()) {
				try {
					map.func_110551_a(mc.func_110442_L());
				} catch (IOException e) {}
			}
		}
		if (maps.containsKey(evt.map.textureType)) {
			maps.get(evt.map.textureType).reregisterIcons();
		}
	}
}
