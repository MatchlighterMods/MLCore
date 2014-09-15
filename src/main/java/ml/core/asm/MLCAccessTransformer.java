package ml.core.asm;

import java.io.IOException;
import java.lang.reflect.Method;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class MLCAccessTransformer extends AccessTransformer {

	public MLCAccessTransformer() throws IOException {
		readMapFile("mlcore_at.cfg");
	}

	private void readMapFile(String mapFile) {
		System.out.println("Adding Accesstransformer map: "+mapFile);
		try {
			Method parentMapFile = AccessTransformer.class.getDeclaredMethod("readMapFile", String.class);
			parentMapFile.setAccessible(true);
			parentMapFile.invoke(this, mapFile);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
