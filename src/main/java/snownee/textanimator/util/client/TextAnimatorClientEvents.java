package snownee.textanimator.util.client;

import java.util.function.Function;

import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import snownee.textanimator.TextAnimator;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.command.TestCommand;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

@EventBusSubscriber(modid = TextAnimator.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class TextAnimatorClientEvents {
	private TextAnimatorClientEvents() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(TextAnimatorClient::init);
	}

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		// Currently unused, but available for client-only hooks when needed.
	}

	@EventBusSubscriber(modid = TextAnimator.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
	public static final class ClientCommandEvents {
		private ClientCommandEvents() {
		}

		@SubscribeEvent
		public static void registerClientCommands(RegisterClientCommandsEvent event) {
			event.getDispatcher().register(Commands.literal("ta_test").executes(ctx -> {
				TestCommand.test();
				return 0;
			}));
		}
	}
}
