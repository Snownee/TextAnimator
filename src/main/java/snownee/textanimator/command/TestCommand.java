package snownee.textanimator.command;

import java.util.Locale;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import snownee.textanimator.effect.EffectFactory;

public class TestCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("ta_test").executes(ctx -> {
			execute();
			return 0;
		}));
	}

	private static void execute() {
		for (String s : EffectFactory.listTypes()) {
			String text = "<%s>  %s %s  ".formatted(s, s, s.toUpperCase(Locale.ENGLISH));
			Minecraft.getInstance().gui.getChat().addMessage(Component.literal(text));
		}
	}
}