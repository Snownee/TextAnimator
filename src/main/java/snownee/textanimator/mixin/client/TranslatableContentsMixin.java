package snownee.textanimator.mixin.client;

import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.datafixers.util.Pair;

import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.StringDecomposer;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.typewriter.TypewriterEffect;
import snownee.textanimator.typewriter.TypewriterTracks;
import snownee.textanimator.util.CommonProxy;

@Mixin(TranslatableContents.class)
public abstract class TranslatableContentsMixin {
	@Inject(method = "decomposeTemplate", at = @At("HEAD"))
	private void textanimator$decomposeTemplate(
			CallbackInfo ci,
			@Local(argsOnly = true) LocalRef<String> stringRef,
			@Share("style") LocalRef<Style> newStyle) {
		String string = stringRef.get();
		Pair<TypewriterEffect, Integer> pair = TypewriterEffect.find(string);
		if (pair == null) {
			return;
		}
		stringRef.set(string.substring(pair.getSecond()));
		Style style = CommonProxy.clone(Style.EMPTY);
		TAStyle taStyle = (TAStyle) style;
		taStyle.textanimator$addEffect(pair.getFirst());
		taStyle.textanimator$setTypewriterTrack(TypewriterTracks.getInstance().get(new Object()));
		newStyle.set(style);
	}

	@WrapOperation(method = "decomposeTemplate", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	private void textanimator$flatmapChars(
			Consumer<FormattedText> instance,
			Object formattedText,
			Operation<Void> original,
			@Share("style") LocalRef<Style> newStyle) {
		Style style = newStyle.get();
		if (style == null) {
			original.call(instance, formattedText);
			return;
		}
		TAStyle taStyle = (TAStyle) style;
		MutableInt charsAccepted = new MutableInt(Math.max(0, taStyle.textanimator$getTypewriterIndex()));
		StringDecomposer.iterateFormatted((FormattedText) formattedText, style, (i, style2, cp) -> {
			instance.accept(FormattedText.of(Character.toString(cp), style2));
			charsAccepted.increment();
			return true;
		});
		style = CommonProxy.clone(style);
		(taStyle).textanimator$setTypewriterIndex(charsAccepted.intValue());
		newStyle.set(style);
	}
}
