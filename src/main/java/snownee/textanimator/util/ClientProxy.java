package snownee.textanimator.util;

import net.fabricmc.api.ClientModInitializer;
import snownee.textanimator.TextAnimatorClient;

public class ClientProxy implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		TextAnimatorClient.init();
	}
}
