package ml.core.asm;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class MLCAccesTransformer extends AccessTransformer {

	public MLCAccesTransformer() throws IOException {
		super("mlcore_at.cfg");
	}

}
