package snownee.textanimator.command;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import snownee.textanimator.api.TextAnimatorAPI;

public class PlayerCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("textanimator")
				.then(ClientCommandManager.literal("toggle")
						.executes(ctx -> toggle(ctx.getSource())))
				.then(ClientCommandManager.literal("status")
						.executes(ctx -> sendStatus(ctx.getSource()))));
		dispatcher.register(ClientCommandManager.literal("ta")
				.then(ClientCommandManager.literal("toggle")
						.executes(ctx -> toggle(ctx.getSource())))
				.then(ClientCommandManager.literal("status")
						.executes(ctx -> sendStatus(ctx.getSource()))));
	}

	private static int toggle(FabricClientCommandSource source) {
		boolean enableParsing = TextAnimatorAPI.isPlayerParsingSuspended();
		return setParsingEnabled(source, enableParsing);
	}

	private static int setParsingEnabled(FabricClientCommandSource source, boolean enableParsing) {
		boolean changed = TextAnimatorAPI.setPlayerParsingSuspended(!enableParsing);
		boolean playerSuspended = TextAnimatorAPI.isPlayerParsingSuspended();
		boolean parsingSuspended = TextAnimatorAPI.isParsingSuspended();
		MutableComponent message;
		if (playerSuspended) {
			message = Component.translatable(changed
					? "textanimator.command.player.enabled"
					: "textanimator.command.player.enabled.already");
		} else {
			message = Component.translatable(changed
					? "textanimator.command.player.disabled"
					: "textanimator.command.player.disabled.already");
			if (parsingSuspended) {
				message = Component.translatable("textanimator.command.player.disabled.external");
			}
		}
		ChatFormatting style = (playerSuspended || parsingSuspended) ? ChatFormatting.YELLOW : ChatFormatting.GREEN;
		source.sendFeedback(message.withStyle(style));
		return changed ? 1 : 0;
	}

	private static int sendStatus(FabricClientCommandSource source) {
		boolean playerSuspended = TextAnimatorAPI.isPlayerParsingSuspended();
		boolean suspended = TextAnimatorAPI.isParsingSuspended();
		MutableComponent message;
		if (suspended) {
			message = Component.translatable(playerSuspended
					? "textanimator.command.player.status.player"
					: "textanimator.command.player.status.external");
		} else {
			message = Component.translatable("textanimator.command.player.status.off");
		}
		source.sendFeedback(message.withStyle(suspended ? ChatFormatting.YELLOW : ChatFormatting.GREEN));
		return suspended ? 1 : 0;
	}
}
