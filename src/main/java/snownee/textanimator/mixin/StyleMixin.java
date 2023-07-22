package snownee.textanimator.mixin;

import java.lang.reflect.Type;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
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

import net.minecraft.network.chat.Style;
import net.minecraft.util.GsonHelper;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;

@Mixin(Style.class)
public class StyleMixin implements TAStyle {

	@Unique
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
		style = style.withClickEvent(style.getClickEvent()); // clone a new one
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
		@Inject(method = "deserialize", at = @At("RETURN"))
		private void textanimator$deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<Style> cir) {
			if (!jsonElement.isJsonObject() || cir.getReturnValue() == null) {
				return;
			}
			JsonObject json = jsonElement.getAsJsonObject();
			if (!json.has("ta$effects")) {
				return;
			}
			ImmutableList.Builder<Effect> builder = ImmutableList.builder();
			GsonHelper.getAsJsonArray(json, "ta$effects").forEach(e -> {
				Effect effect = Effect.create(StringUtils.split(e.getAsString(), ' '));
				if (effect != null) {
					builder.add(effect);
				}
			});
			((TAStyle) cir.getReturnValue()).textanimator$setEffects(builder.build());
		}

		@Inject(method = "serialize", at = @At("RETURN"))
		private void textanimator$serialize(Style style, Type type, JsonSerializationContext jsonSerializationContext, CallbackInfoReturnable<JsonElement> cir) {
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
