package snownee.textanimator.util;

import net.fabricmc.api.ClientModInitializer;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

import java.util.function.Function;

public class ClientProxy implements ClientModInitializer {
        public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
        }

	@Override
	public void onInitializeClient() {
		TextAnimatorClient.init();
	}
}
