package ml.core.asm;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class MLCAccessTransformer extends AccessTransformer {

	public MLCAccessTransformer() throws IOException {
		super("mlcore_at.cfg");
	}

}
