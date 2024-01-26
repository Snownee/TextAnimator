package snownee.textanimator.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.RainbowEffect;

@Mixin(Font.class)
public class FontMixin {
	@ModifyExpressionValue(
			method = {"lambda$drawInBatch8xOutline$1", "m_168654_", "method_37297"},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/chat/Style;withColor(I)Lnet/minecraft/network/chat/Style;"
			)
	)
	private Style textanimator$removeRainbowForBorder(Style original) {
		final var style = (TAStyle) original;
		if (style.textanimator$getEffects().isEmpty()) return original;
		style.textanimator$setEffects(style.textanimator$getEffects()
										   .stream()
										   .filter(it -> !(it instanceof RainbowEffect))
										   .collect(
												   ImmutableList.toImmutableList()));
		return original;
	}
}
