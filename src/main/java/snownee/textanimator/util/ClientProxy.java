package snownee.textanimator.util;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkConstants;
import snownee.textanimator.TextAnimatorClient;

@Mod("textanimator")
public class ClientProxy {

	public ClientProxy() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		if (FMLEnvironment.dist.isClient()) {
			TextAnimatorClient.init();
		}
	}

}
