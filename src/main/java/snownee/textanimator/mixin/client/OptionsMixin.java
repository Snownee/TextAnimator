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
import snownee.textanimator.TextAnimationStatus;
import snownee.textanimator.duck.TAOptions;

@Mixin(Options.class)
public class OptionsMixin implements TAOptions {
	@Unique
	private final OptionInstance<TextAnimationStatus> textAnimator$textAnimation = new OptionInstance<>("options.textanimator", OptionInstance.noTooltip(), OptionInstance.forOptionEnum(), new OptionInstance.Enum<>(Arrays.asList(TextAnimationStatus.values()), Codec.INT.xmap(TextAnimationStatus::byId, TextAnimationStatus::getId)), TextAnimationStatus.ALL, status -> {
	});

	@Inject(method = "processOptions", at = @At("RETURN"))
	private void textanimator$processOptions(Options.FieldAccess fieldAccess, CallbackInfo ci) {
		fieldAccess.process("textAnimation", textAnimator$textAnimation);
	}

	@Override
	public OptionInstance<TextAnimationStatus> textanimator$getOption() {
		return textAnimator$textAnimation;
	}
}
