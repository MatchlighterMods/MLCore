package ml.core.texture;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
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
		map.refreshTextures();
	}
	
	@ForgeSubscribe
	public void reregisterIcons(TextureStitchEvent.Pre evt) {
		if (evt.map.textureType==0) {
			for (TextureMap map : maps.values()) {
				map.refreshTextures();
			}
		}
		if (maps.containsKey(evt.map.textureType)) {
			maps.get(evt.map.textureType).reregisterIcons();
		}
	}
}
