package snownee.textanimator.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "textanimator", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PreviewCommand {
	@SubscribeEvent
	public static void register(RegisterClientCommandsEvent event) {
		var dispatcher = event.getDispatcher();
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("ta_preview");

		RequiredArgumentBuilder<CommandSourceStack, String> effectArg = Commands.argument("effect", StringArgumentType.greedyString())
				.executes(ctx -> {
					String effect = StringArgumentType.getString(ctx, "effect");
					show("<" + effect + ">Text Animator 预览</" + effect + ">");
					return 1;
				});

		root.executes(ctx -> {
			show("<glitch intensity=1.2 freq=3 shift=0.4 flicker=0.15><fade minA=0.5 speed=1.3><rainb>Mafuyu404 never like you</rainb></fade></glitch>");
			show("<wave><gradient start=#7FFFD4 end=#1E90FF speed=0.3><bounce amp=2.5 speed=1.0>Mafuyu404 never like you</bounce></gradient></wave>");
			show("<blur passes=8 radius=2 alpha=0.18><pulse base=0.8 amp=0.4 speed=2.5><gradient start=#FF0080 end=#00FFFF speed=0.8>Mafuyu404 never like you</gradient></pulse></blur>");
			show("<turbulence amp=2 speed=2><glitch intensity=1.3 freq=4 shift=0.5 flicker=0.25><pulse base=0.7 amp=0.3 speed=2.2>Mafuyu404 never like you</pulse></glitch></turbulence>");
			show("<rotate speed=1.0 range=0.5 radius=2><gradient start=#FF69B4 end=#87CEFA speed=0.5><fade minA=0.4 speed=1.5>Mafuyu404 never like you</fade></gradient></rotate>");
//            show("<bounce amp=3>Bounce 弹跳</bounce>");
//            show("<swing amp=2>Swing 摆动</swing>");
//            show("<rotate radius=1.5>Rotate 旋转路径</rotate>");
//            show("<fade minA=0.2>Fade 透明</fade>");
//            show("<shadow dx=1 dy=1 r=0 g=0 b=0 a=0.9>Shadow 阴影</shadow>");
//            show("<blur>Blur 模糊</blur>");
//            show("<scroll speed=1>Scroll 字幕</scroll>");
			show("<glitch intensity=1.0 flicker=0.2 shift=0.6>⚠️ CORE BREACH ⚠️</glitch>");
			return 1;
		});

	}

	private static void show(String text) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null) {
			player.sendSystemMessage(Component.literal(text));
		} else {
			Minecraft.getInstance().gui.getChat().addMessage(Component.literal(text));
		}
	}
}