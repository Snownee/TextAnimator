package snownee.textanimator.util;

import java.util.function.Function;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.command.TestCommand;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

public class ClientProxy implements ClientModInitializer {
	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
	}

	@Override
	public void onInitializeClient() {
		TextAnimatorClient.init();
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			TestCommand.register(dispatcher);
		});
	}
}
