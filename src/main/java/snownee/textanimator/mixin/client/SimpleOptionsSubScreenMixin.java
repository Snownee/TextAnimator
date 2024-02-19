package snownee.textanimator.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.ChatOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.Component;
import snownee.textanimator.duck.TAOptions;

@Mixin(SimpleOptionsSubScreen.class)
public class SimpleOptionsSubScreenMixin {
	@Mutable
	@Final
	@Shadow
	protected OptionInstance<?>[] smallOptions;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void textanimator$init(
			Screen parent,
			Options options,
			Component component,
			OptionInstance<?>[] optionInstances,
			CallbackInfo ci) {
		Screen screen = (Screen) (Object) this;
		if (screen.getClass() == ChatOptionsScreen.class) {
			OptionInstance<?>[] newInstances = new OptionInstance<?>[smallOptions.length + 3];
			System.arraycopy(smallOptions, 0, newInstances, 0, smallOptions.length);
			newInstances[smallOptions.length] = ((TAOptions) options).textanimator$getTextAnimation();
			newInstances[smallOptions.length + 1] = ((TAOptions) options).textanimator$getTypewriterSpeed();
			newInstances[smallOptions.length + 2] = ((TAOptions) options).textanimator$getTypewriterMode();
			smallOptions = newInstances;
		}
	}
}
