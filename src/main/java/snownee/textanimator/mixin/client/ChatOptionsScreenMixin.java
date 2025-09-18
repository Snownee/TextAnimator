package snownee.textanimator.mixin.client;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import snownee.textanimator.duck.TAOptions;

import java.util.Arrays;

@Mixin(ChatOptionsScreen.class)
public class ChatOptionsScreenMixin {
        @Inject(
                        method = "options(Lnet/minecraft/client/Options;)[Lnet/minecraft/client/OptionInstance;",
                        at = @At("RETURN"),
                        cancellable = true)
        private static void textanimator$appendOptions(
                        Options options,
                        CallbackInfoReturnable<OptionInstance<?>[]> cir) {
                OptionInstance<?>[] original = cir.getReturnValue();
                OptionInstance<?>[] extended = Arrays.copyOf(original, original.length + 3);
                TAOptions taOptions = (TAOptions) options;
                extended[original.length] = taOptions.textanimator$getTextAnimation();
                extended[original.length + 1] = taOptions.textanimator$getTypewriterSpeed();
                extended[original.length + 2] = taOptions.textanimator$getTypewriterMode();
                cir.setReturnValue(extended);
        }
}
