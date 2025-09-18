package snownee.textanimator.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;

@Mixin(StringDecomposer.class)
public interface StringDecomposerAccess {
	@Invoker
	static boolean callFeedChar(Style p_14333_, FormattedCharSink p_14334_, int p_14335_, char p_14336_) {
		throw new IllegalAccessError("Shouldn't be called");
	}
}
