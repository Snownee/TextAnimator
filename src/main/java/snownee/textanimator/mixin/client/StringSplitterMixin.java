package snownee.textanimator.mixin.client;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;
import snownee.textanimator.duck.TAStyle;

@Mixin(StringSplitter.class)
public class StringSplitterMixin {
	@WrapOperation(
			method = "splitLines(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;Ljava/util/function/BiConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/chat/FormattedText;visit(Lnet/minecraft/network/chat/FormattedText$StyledContentConsumer;Lnet/minecraft/network/chat/Style;)Ljava/util/Optional;"))
	private Optional<Object> textanimator$visitFormattedText(
			FormattedText formattedText,
			FormattedText.StyledContentConsumer<Object> tStyledContentConsumer,
			Style style,
			Operation<Optional<Object>> original,
			@Local List<StringSplitter.LineComponent> list) {
		return formattedText.visit((stylex, string) -> {
			if (string.isEmpty()) {
				return Optional.empty();
			}
			TAStyle taStyle = (TAStyle) stylex;
			if (taStyle.textanimator$getTypewriterTrack() != null) {
				StringDecomposer.iterateFormatted(string, stylex, (i, style2, cp) -> {
					list.add(new StringSplitter.LineComponent(Character.toString(cp), style2));
					return true;
				});
			} else {
				list.add(new StringSplitter.LineComponent(string, stylex));
			}
			return Optional.empty();
		}, style);
	}
}
