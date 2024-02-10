package snownee.textanimator.util;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.duck.TATranslatableContents;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.typewriter.TypewriterTracks;

public class ClientProxy implements ClientModInitializer {
	public static void createComponent(MutableComponent component) {
		ComponentContents contents = component.contents;
		if (contents instanceof LiteralContents || contents instanceof TranslatableContents) {
			if (contents instanceof TATranslatableContents translatableContents) {
				translatableContents.textanimator$setStart(0);
			}
			String string = component.getString();
			if (string.contains("Type")) {
				System.out.println("11"+string);
			}
			if (!string.startsWith("<typewriter")) {
				return;
			}
			int endIndex = string.indexOf('>');
			if (endIndex == -1) {
				return;
			}
			Effect effect = Effect.create(string.substring(1, endIndex), true);
			if (effect == null) {
				return;
			}
			Style style = CommonProxy.clone(component.getStyle());
			TAStyle taStyle = (TAStyle) style;
			taStyle.textanimator$setTypewriterTrack(TypewriterTracks.getInstance().get(new Object()));
			taStyle.textanimator$addEffect(effect);
			if (contents instanceof TATranslatableContents translatableContents) {
				translatableContents.textanimator$setStart(endIndex + 1);
			} else {
				component.contents = new LiteralContents(string.substring(endIndex + 1));
			}
			component.setStyle(style);
		}
	}

	@Override
	public void onInitializeClient() {
		TextAnimatorClient.init();
	}
}
