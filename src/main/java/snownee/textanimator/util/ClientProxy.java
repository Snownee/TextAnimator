package snownee.textanimator.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import snownee.textanimator.TextAnimatorClient;

@Mod("textanimator")
public class ClientProxy {

	public ClientProxy() {
		if (FMLEnvironment.dist.isClient()) {
			TextAnimatorClient.init();
		}
	}

}
