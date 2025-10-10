package snownee.textanimator.util;

import java.util.function.Function;

import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.command.TestCommand;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

@EventBusSubscriber(modid = CommonProxy.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientProxy {

	private ClientProxy() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(TextAnimatorClient::init);
	}

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		// Currently unused, but available for client-only hooks when needed.
	}

	public static void init() {
		NeoForge.EVENT_BUS.addListener(
				RegisterClientCommandsEvent.class, event -> {
					event.getDispatcher().register(Commands.literal("ta_test").executes(ctx -> {
						TestCommand.test();
						return 0;
					}));
				});
	}
}
