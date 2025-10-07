package snownee.textanimator.util;

import java.util.function.Function;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.command.TestCommand;
import snownee.textanimator.compat.HermesCompat;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

public class ClientProxy implements ClientModInitializer {
	private static final boolean hermes = FabricLoader.getInstance().isModLoaded("hermes");

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		if (hermes) {
			HermesCompat.onEffectTypeRegistered(type, factory);
		}
	}

	@Override
	public void onInitializeClient() {
		TextAnimatorClient.init();
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("ta_test").executes(ctx -> {
				TestCommand.test();
				return 0;
			}));
		});
	}
}
