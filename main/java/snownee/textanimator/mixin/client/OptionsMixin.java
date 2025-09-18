package snownee.textanimator.mixin.client;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.Codec;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import snownee.textanimator.TextAnimationMode;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.TypewriterMode;
import snownee.textanimator.duck.TAOptions;

@Mixin(Options.class)
public class OptionsMixin implements TAOptions {
	@Unique
	private OptionInstance<TextAnimationMode> textanimator$textAnimation;
	@Unique
	private OptionInstance<Integer> textanimator$typewriterSpeed;
	@Unique
	private OptionInstance<TypewriterMode> textanimator$typewriterMode;

	@Inject(method = "load", at = @At("HEAD"))
	private void textanimator$load(CallbackInfo ci) {
		createExtraOptions();
	}

	@Inject(method = "processOptions", at = @At("RETURN"))
	private void textanimator$processOptions(Options.FieldAccess fieldAccess, CallbackInfo ci) {
		createExtraOptions();
		fieldAccess.process("textanimator.textAnimation", textanimator$textAnimation);
		fieldAccess.process("textanimator.typewriterSpeed", textanimator$typewriterSpeed);
		fieldAccess.process("textanimator.typewriterMode", textanimator$typewriterMode);
		TextAnimatorClient.setTypewriterSpeed(textanimator$typewriterSpeed.get());
	}

	@Override
	public OptionInstance<TextAnimationMode> textanimator$getTextAnimation() {
		return textanimator$textAnimation;
	}

	@Override
	public OptionInstance<Integer> textanimator$getTypewriterSpeed() {
		return textanimator$typewriterSpeed;
	}

	@Override
	public OptionInstance<TypewriterMode> textanimator$getTypewriterMode() {
		return textanimator$typewriterMode;
	}

	@Unique
	private void createExtraOptions() {
		if (textanimator$textAnimation != null) {
			return;
		}
		textanimator$textAnimation = new OptionInstance<>(
				"options.textanimator.animation",
				OptionInstance.noTooltip(),
				OptionInstance.forOptionEnum(),
				new OptionInstance.Enum<>(
						Arrays.asList(TextAnimationMode.values()),
						Codec.INT.xmap(TextAnimationMode::byId, TextAnimationMode::getId)),
				TextAnimationMode.ALL,
				status -> {
				});

		textanimator$typewriterSpeed = new OptionInstance<>(
				"options.textanimator.typewriterSpeed",
				OptionInstance.noTooltip(),
				(component, integer) -> Options.genericValueLabel(
						component,
						Component.translatable("options.textanimator.typewriterSpeed.value", integer)),
				new OptionInstance.IntRange(1, 9),
				5,
				TextAnimatorClient::setTypewriterSpeed);

		textanimator$typewriterMode = new OptionInstance<>(
				"options.textanimator.typewriterMode",
				OptionInstance.noTooltip(),
				OptionInstance.forOptionEnum(),
				new OptionInstance.Enum<>(
						Arrays.asList(TypewriterMode.values()),
						Codec.INT.xmap(TypewriterMode::byId, TypewriterMode::getId)),
				TypewriterMode.BY_CHAR,
				status -> {
				});
	}
}
