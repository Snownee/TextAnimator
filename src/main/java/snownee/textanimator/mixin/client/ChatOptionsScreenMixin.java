package snownee.textanimator.mixin.client;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
import snownee.textanimator.duck.TAOptions;

@Mixin(ChatOptionsScreen.class)
public class ChatOptionsScreenMixin {
	@WrapMethod(method = "options")
	private static OptionInstance<?>[] textanimator$appendOptions(Options options, Operation<OptionInstance<?>[]> original) {
		OptionInstance<?>[] instances = original.call(options);
		OptionInstance<?>[] extended = Arrays.copyOf(instances, instances.length + 3);
		TAOptions taOptions = (TAOptions) options;
		extended[instances.length] = taOptions.textanimator$getTextAnimation();
		extended[instances.length + 1] = taOptions.textanimator$getTypewriterSpeed();
		extended[instances.length + 2] = taOptions.textanimator$getTypewriterMode();
		return extended;
	}
}
