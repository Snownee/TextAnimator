package snownee.textanimator.command;

import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import snownee.textanimator.effect.EffectFactory;

public class TestCommand {
	public static void test() {
		for (String s : EffectFactory.listTypes()) {
			String text = "<%s>  %s %s  ".formatted(s, s, s.toUpperCase(Locale.ENGLISH));
			Minecraft.getInstance().gui.getChat().addMessage(Component.literal(text));
		}
	}
}