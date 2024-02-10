package snownee.textanimator.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.duck.TATranslatableContents;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.typewriter.TypewriterTracks;
import snownee.textanimator.util.CommonProxy;

@Mixin(MutableComponent.class)
public class MutableComponentMixin {
	@Inject(method = "create", at = @At("RETURN"))
	private static void textanimator$create(ComponentContents componentContents, CallbackInfoReturnable<MutableComponent> cir) {
		MutableComponent component = cir.getReturnValue();
		ComponentContents contents = component.contents;
		if (contents instanceof LiteralContents || contents instanceof TranslatableContents) {
			String string = component.getString();
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
}
