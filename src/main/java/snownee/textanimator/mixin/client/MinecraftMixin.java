package snownee.textanimator.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import snownee.textanimator.TextAnimatorApi;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "runTick", at = @At("HEAD"))
	private void textanimator$runTick(boolean bl, CallbackInfo ci) {
		TextAnimatorApi.resetParsingState();
	}
}
