package snownee.textanimator.util;

import java.util.function.Function;

import net.fabricmc.api.ClientModInitializer;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

public class ClientProxy implements ClientModInitializer {
        public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
                // no-op, reserved for future client-only integrations
        }

	@Override
	public void onInitializeClient() {
		TextAnimatorClient.init();
	}
}
