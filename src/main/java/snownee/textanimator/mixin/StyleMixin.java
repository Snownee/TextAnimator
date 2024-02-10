package snownee.textanimator.mixin;

import java.lang.reflect.Type;
import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.network.chat.Style;
import net.minecraft.util.GsonHelper;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.typewriter.TypewriterTrack;

@Mixin(Style.class)
public class StyleMixin implements TAStyle {
	@Unique
	private ImmutableList<Effect> textanimator$effects = ImmutableList.of();
	@Unique
	private TypewriterTrack textanimator$track;
	@Unique
	private int textanimator$typewriterIndex = -1;

	@Override
	public ImmutableList<Effect> textanimator$getEffects() {
		return textanimator$effects;
	}

	@Override
	public void textanimator$setEffects(ImmutableList<Effect> effects) {
		textanimator$effects = effects;
	}

	@Override
	public void textanimator$addEffect(Effect effect) {
		if (textanimator$effects.isEmpty()) {
			textanimator$effects = ImmutableList.of(effect);
		} else {
			textanimator$effects = ImmutableList.<Effect>builder().addAll(textanimator$effects).add(effect).build();
		}
	}

	@Override
	public TypewriterTrack textanimator$getTypewriterTrack() {
		return textanimator$track;
	}

	@Override
	public void textanimator$setTypewriterTrack(TypewriterTrack track) {
		textanimator$track = track;
	}

	@Override
	public int textanimator$getTypewriterIndex() {
		return textanimator$typewriterIndex;
	}

	@Override
	public void textanimator$setTypewriterIndex(int index) {
		textanimator$typewriterIndex = index;
	}

	@ModifyReturnValue(
			method = {"withColor(Lnet/minecraft/network/chat/TextColor;)Lnet/minecraft/network/chat/Style;",
					"withBold", "withItalic", "withUnderlined", "withStrikethrough", "withObfuscated",
					"withClickEvent", "withHoverEvent", "withInsertion", "withFont", "applyFormat",
					"applyLegacyFormat", "applyFormats"},
			at = @At("RETURN")
	)
	private Style textanimator$applyTo(final Style original) {
		Style self = (Style) (Object) this;
		if (self == original) return original;
		if (textanimator$getEffects().isEmpty() && textanimator$getTypewriterTrack() == null) return original;
		((TAStyle) original).textanimator$setEffects(textanimator$getEffects());
		if (textanimator$getTypewriterTrack() != null) {
			((TAStyle) original).textanimator$setTypewriterTrack(textanimator$getTypewriterTrack());
			((TAStyle) original).textanimator$setTypewriterIndex(textanimator$getTypewriterIndex());
		}
		return original;
	}

	@ModifyReturnValue(method = "applyTo", at = @At("RETURN"))
	private Style textanimator$applyTo(final Style original, final Style that) {
		Style self = (Style) (Object) this;
		if (self == original || that == original) return original;
		ImmutableList<Effect> effects = textanimator$getEffects();
		TypewriterTrack track = textanimator$getTypewriterTrack();
		int index = textanimator$getTypewriterIndex();
		TAStyle thatStyle = (TAStyle) that;
		ImmutableList<Effect> thatEffects = thatStyle.textanimator$getEffects();
		TypewriterTrack thatTrack = thatStyle.textanimator$getTypewriterTrack();
		int thatIndex = thatStyle.textanimator$getTypewriterIndex();
		TAStyle originalStyle = (TAStyle) original;
		// should we merge effects?
		originalStyle.textanimator$setEffects(effects.isEmpty() ? thatEffects : effects);
		originalStyle.textanimator$setTypewriterTrack(track == null ? thatTrack : track);
		originalStyle.textanimator$setTypewriterIndex(index == -1 ? thatIndex : index);
		return original;
	}

	@Inject(method = "equals", at = @At("HEAD"), cancellable = true)
	private void textanimator$equals(Object obj, CallbackInfoReturnable<Boolean> cir) {
		if (this != obj && obj instanceof TAStyle style) {
			if (!Objects.equals(this.textanimator$getEffects(), style.textanimator$getEffects())) {
				cir.setReturnValue(false);
			}
			if (!Objects.equals(this.textanimator$getTypewriterTrack(), style.textanimator$getTypewriterTrack())) {
				cir.setReturnValue(false);
			}
		}
	}

	@Mixin(Style.Serializer.class)
	public static class SerializerMixin {
		@Inject(method = "deserialize", at = @At("RETURN"))
		private void textanimator$deserialize(
				JsonElement jsonElement,
				Type type,
				JsonDeserializationContext jsonDeserializationContext,
				CallbackInfoReturnable<Style> cir
		) {
			if (!jsonElement.isJsonObject() || cir.getReturnValue() == null) {
				return;
			}
			JsonObject json = jsonElement.getAsJsonObject();
			if (!json.has("ta$effects")) {
				return;
			}
			ImmutableList.Builder<Effect> builder = ImmutableList.builder();
			GsonHelper.getAsJsonArray(json, "ta$effects").forEach(e -> {
				Effect effect = Effect.create(e.getAsString(), true);
				if (effect != null) {
					builder.add(effect);
				}
			});
			((TAStyle) cir.getReturnValue()).textanimator$setEffects(builder.build());
		}

		@Inject(method = "serialize", at = @At("RETURN"))
		private void textanimator$serialize(
				Style style,
				Type type,
				JsonSerializationContext jsonSerializationContext,
				CallbackInfoReturnable<JsonElement> cir
		) {
			JsonElement jsonElement = cir.getReturnValue();
			TAStyle taStyle = (TAStyle) style;
			if (jsonElement == null || !jsonElement.isJsonObject() || taStyle.textanimator$getEffects().isEmpty()) {
				return;
			}
			JsonObject json = jsonElement.getAsJsonObject();
			JsonArray array = new JsonArray();
			taStyle.textanimator$getEffects().forEach(e -> array.add(e.serialize()));
			json.add("ta$effects", array);
		}
	}
}
