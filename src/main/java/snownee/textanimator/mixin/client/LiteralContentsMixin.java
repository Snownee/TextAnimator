package snownee.textanimator.mixin.client;

import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.util.StringDecomposer;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.typewriter.TypewriterEffect;
import snownee.textanimator.typewriter.TypewriterTracks;
import snownee.textanimator.util.CommonProxy;

@Mixin(LiteralContents.class)
public abstract class LiteralContentsMixin {
	@Shadow
	@Final
	private String text;

	@Inject(method = "visit(Lnet/minecraft/network/chat/FormattedText$StyledContentConsumer;Lnet/minecraft/network/chat/Style;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
	private <T> void textanimator$visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style, CallbackInfoReturnable<Optional<T>> cir) {
		Pair<TypewriterEffect, Integer> pair = TypewriterEffect.find(text);
		if (pair == null) {
			return;
		}
		style = CommonProxy.clone(style);
		TAStyle taStyle = (TAStyle) style;
		taStyle.textanimator$addEffect(pair.getFirst());
		taStyle.textanimator$setTypewriterTrack(TypewriterTracks.getInstance().get(text.intern()));
		String realText = text.substring(pair.getSecond());
		MutableObject<Optional<T>> result = new MutableObject<>(Optional.empty());
		StringDecomposer.iterateFormatted(realText, style, (i, style2, cp) -> {
			result.setValue(styledContentConsumer.accept(style2, Character.toString(cp)));
			return result.getValue().isEmpty();
		});
		cir.setReturnValue(result.getValue());
	}
}
