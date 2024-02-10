package snownee.textanimator.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import snownee.textanimator.duck.TALineBreakFinder;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.util.CommonProxy;

@Mixin(StringSplitter.class)
public class StringSplitterMixin {
	@WrapOperation(method = "splitLines(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"))
	private boolean textanimator$iterateFormatted(String string, int i, Style style, Style style2, FormattedCharSink formattedCharSink, Operation<Boolean> original, @Share("index") LocalIntRef index) {
		TAStyle taStyle = (TAStyle) style;
		if (taStyle.textanimator$getTypewriterTrack() != null) {
			if (index.get() == 0) {
				index.set(taStyle.textanimator$getTypewriterIndex());
			}
			if (index.get() > 0) {
				style = CommonProxy.clone(style);
				taStyle = (TAStyle) style;
				taStyle.textanimator$setTypewriterIndex(index.get());
			}
		}
		boolean result = original.call(string, i, style, style2, formattedCharSink);
		if (taStyle.textanimator$getTypewriterTrack() != null && formattedCharSink instanceof StringSplitter.LineBreakFinder finder) {
			index.set(taStyle.textanimator$getTypewriterIndex() + finder.getSplitPosition() - ((TALineBreakFinder) finder).textanimator$getSkippedChars());
		}
		return result;
	}
}
