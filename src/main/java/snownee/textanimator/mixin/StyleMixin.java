package snownee.textanimator.mixin;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;

@Mixin(Style.class)
public class StyleMixin implements TAStyle {

	@Final
	@Shadow
	@Nullable
	TextColor color;
	@Final
	@Shadow
	@Nullable
	Boolean bold;
	@Final
	@Shadow
	@Nullable
	Boolean italic;
	@Final
	@Shadow
	@Nullable
	Boolean underlined;
	@Final
	@Shadow
	@Nullable
	Boolean strikethrough;
	@Final
	@Shadow
	@Nullable
	Boolean obfuscated;
	@Final
	@Shadow
	@Nullable
	ClickEvent clickEvent;
	@Final
	@Shadow
	@Nullable
	HoverEvent hoverEvent;
	@Final
	@Shadow
	@Nullable
	String insertion;
	@Final
	@Shadow
	@Nullable
	ResourceLocation font;
	private ImmutableList<Effect> textanimator$effects = ImmutableList.of();

	@Override
	public ImmutableList<Effect> textanimator$getEffects() {
		return textanimator$effects;
	}

	@Override
	public void textanimator$setEffects(ImmutableList<Effect> effects) {
		textanimator$effects = effects;
	}

	@Inject(method = "applyTo", at = @At("HEAD"), cancellable = true)
	private void textanimator$applyTo(Style style, CallbackInfoReturnable<Style> cir) {
		Style $this = (Style) (Object) this;
		if ($this == Style.EMPTY) {
			cir.setReturnValue(style);
			return;
		} else if (style == Style.EMPTY) {
			cir.setReturnValue($this);
			return;
		}
		style = new Style(this.color != null ? this.color : style.getColor(), this.bold != null ? this.bold : style.isBold(), this.italic != null ? this.italic : style.isItalic(), this.underlined != null ? this.underlined : style.isUnderlined(), this.strikethrough != null ? this.strikethrough : style.isStrikethrough(), this.obfuscated != null ? this.obfuscated : style.isObfuscated(), this.clickEvent != null ? this.clickEvent : style.getClickEvent(), this.hoverEvent != null ? this.hoverEvent : style.getHoverEvent(), this.insertion != null ? this.insertion : style.getInsertion(), this.font != null ? this.font : style.getFont());
		((TAStyle) style).textanimator$setEffects(((TAStyle) $this).textanimator$getEffects());
		cir.setReturnValue(style);
	}

	@Inject(method = "equals", at = @At("HEAD"), cancellable = true)
	private void textanimator$equals(Object obj, CallbackInfoReturnable<Boolean> cir) {
		if (this != obj && obj instanceof TAStyle style) {
			if (!Objects.equals(this.textanimator$getEffects(), style.textanimator$getEffects())) {
				cir.setReturnValue(false);
			}
		}
	}

	@Mixin(Style.Serializer.class)
	public static class SerializerMixin {
		// TODO
	}
}
