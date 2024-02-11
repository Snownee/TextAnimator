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
import snownee.textanimator.TextAnimationStatus;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.duck.TAOptions;

@Mixin(Options.class)
public class OptionsMixin implements TAOptions {
	@Unique
	private OptionInstance<TextAnimationStatus> textanimator$textAnimation;

	@Unique
	private OptionInstance<Integer> textanimator$typewriterSpeed;

	@Inject(method = "load", at = @At("HEAD"))
	private void textanimator$load(CallbackInfo ci) {
		createExtraOptions();
	}

	@Inject(method = "processOptions", at = @At("RETURN"))
	private void textanimator$processOptions(Options.FieldAccess fieldAccess, CallbackInfo ci) {
		createExtraOptions();
		fieldAccess.process("textanimator.textAnimation", textanimator$textAnimation);
		fieldAccess.process("textanimator.typewriterSpeed", textanimator$typewriterSpeed);
		TextAnimatorClient.setTypewriterSpeed(textanimator$typewriterSpeed.get());
	}

	@Override
	public OptionInstance<TextAnimationStatus> textanimator$getTextAnimation() {
		return textanimator$textAnimation;
	}

	@Override
	public OptionInstance<Integer> textanimator$getTypewriterSpeed() {
		return textanimator$typewriterSpeed;
	}

	@Unique
	private void createExtraOptions() {
		if (textanimator$textAnimation != null) {
			return;
		}
		textanimator$textAnimation = new OptionInstance<>("options.textanimator.animation",
				OptionInstance.noTooltip(),
				OptionInstance.forOptionEnum(),
				new OptionInstance.Enum<>(
						Arrays.asList(TextAnimationStatus.values()),
						Codec.INT.xmap(TextAnimationStatus::byId, TextAnimationStatus::getId)),
				TextAnimationStatus.ALL, status -> {
		});

		textanimator$typewriterSpeed = new OptionInstance<>("options.textanimator.typewriterSpeed",
				OptionInstance.noTooltip(),
				(component, integer) -> Options.genericValueLabel(component, Component.translatable("options.textanimator.typewriterSpeed.value", integer)),
				new OptionInstance.IntRange(1, 9),
				5,
				TextAnimatorClient::setTypewriterSpeed);
	}
}
