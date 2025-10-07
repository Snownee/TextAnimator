package snownee.textanimator.util;

import java.util.Locale;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import snownee.textanimator.command.TestCommand;
import snownee.textanimator.compat.HermesCompat;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

public class ClientProxy {
	private static final boolean hermes = ModList.get().isLoaded("hermes");

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		if (hermes) {
			HermesCompat.onEffectTypeRegistered(type, factory);
		}
	}

	public static Locale getLocale() {
		return Minecraft.getInstance().getLocale();
	}

	public static void init() {
		MinecraftForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) -> {
			event.getDispatcher().register(Commands.literal("ta_test").executes(ctx -> {
				TestCommand.test();
				return 0;
			}));
		});
	}
}
