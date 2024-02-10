package snownee.textanimator.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import snownee.textanimator.util.ClientProxy;

@Mixin(MutableComponent.class)
public class MutableComponentMixin {
	@Inject(method = "create", at = @At("RETURN"))
	private static void textanimator$create(ComponentContents componentContents, CallbackInfoReturnable<MutableComponent> cir) {
		ClientProxy.createComponent(cir.getReturnValue());
	}
}
