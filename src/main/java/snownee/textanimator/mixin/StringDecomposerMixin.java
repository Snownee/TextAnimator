package snownee.textanimator.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import snownee.textanimator.util.CommonProxy;

@Mixin(value = StringDecomposer.class, priority = 1200)
public abstract class StringDecomposerMixin {
	@Inject(method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", at = @At("HEAD"), cancellable = true)
	private static void textanimator$iterateFormatted(String string, int i, Style style, Style plainStyle, FormattedCharSink formattedCharSink, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(CommonProxy.iterateFormatted(string, i, style, plainStyle, formattedCharSink));
	}
}
