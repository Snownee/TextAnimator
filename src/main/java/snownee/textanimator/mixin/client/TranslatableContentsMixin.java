package snownee.textanimator.mixin.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.contents.TranslatableContents;
import snownee.textanimator.duck.TATranslatableContents;

@Mixin(TranslatableContents.class)
public abstract class TranslatableContentsMixin implements TATranslatableContents {
	@Shadow
	@Nullable
	private Language decomposedWith;

	@Shadow
	protected abstract void decompose();

	@Unique
	private int textanimator$start;

	@Override
	public void textanimator$setStart(int index) {
		textanimator$start = index;
		decomposedWith = null;
		decompose();
	}

	@Inject(method = "decompose", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;builder()Lcom/google/common/collect/ImmutableList$Builder;"))
	private void textanimator$decompose(CallbackInfo ci, @Local LocalRef<String> stringRef) {
		if (textanimator$start > 0) {
			String s = stringRef.get();
			if (textanimator$start < s.length()) {
				stringRef.set(s.substring(textanimator$start));
			}
		}
	}
}
