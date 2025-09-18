package snownee.textanimator.mixin;

import java.util.Objects;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.network.chat.Style;
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
			method = {
					"withColor(Lnet/minecraft/network/chat/TextColor;)Lnet/minecraft/network/chat/Style;",
					"withBold",
					"withItalic",
					"withUnderlined",
					"withStrikethrough",
					"withObfuscated",
					"withClickEvent",
					"withHoverEvent",
					"withInsertion",
					"withFont",
					"applyFormat",
					"applyLegacyFormat",
					"applyFormats"}, at = @At("RETURN"))
	private Style textanimator$applyTo(final Style original) {
		Style self = (Style) (Object) this;
		if (self == original) {
			return original;
		}
		if (textanimator$getEffects().isEmpty() && textanimator$getTypewriterTrack() == null) {
			return original;
		}
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
		if (self == original || that == original) {
			return original;
		}
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
			if (this.textanimator$getTypewriterIndex() != style.textanimator$getTypewriterIndex()) {
				cir.setReturnValue(false);
			}
		}
	}

        @Mixin(Style.Serializer.class)
        public static class SerializerMixin {
                @Mutable
                @Shadow
                @Final
                private static MapCodec<Style> MAP_CODEC;

                @Mutable
                @Shadow
                @Final
                private static Codec<Style> CODEC;

                @Inject(method = "<clinit>", at = @At("RETURN"))
                private static void textanimator$attachCustomCodec(CallbackInfo ci) {
                        MAP_CODEC = textanimator$wrapCodec(MAP_CODEC);
                        CODEC = MAP_CODEC.codec();
                }

                @Unique
                private static MapCodec<Style> textanimator$wrapCodec(MapCodec<Style> original) {
                        return new MapCodec<>() {
                                @Override
                                public <T> DataResult<Style> decode(DynamicOps<T> ops, MapLike<T> input) {
                                        DataResult<Style> result = original.decode(ops, input);
                                        T raw = input.get("ta$effects");
                                        if (raw == null) {
                                                return result;
                                        }
                                        JsonElement jsonElement = ops.convertTo(JsonOps.INSTANCE, raw);
                                        if (!jsonElement.isJsonArray()) {
                                                return result;
                                        }
                                        ImmutableList.Builder<Effect> builder = ImmutableList.builder();
                                        JsonArray array = jsonElement.getAsJsonArray();
                                        for (JsonElement entry : array) {
                                                try {
                                                        builder.add(Effect.create(entry.getAsString(), true));
                                                } catch (Exception ignored) {
                                                }
                                        }
                                        ImmutableList<Effect> effects = builder.build();
                                        if (effects.isEmpty()) {
                                                return result;
                                        }
                                        return result.map(style -> {
                                                ((TAStyle) style).textanimator$setEffects(effects);
                                                return style;
                                        });
                                }

                                @Override
                                public <T> RecordBuilder<T> encode(Style style, DynamicOps<T> ops, RecordBuilder<T> builder) {
                                        RecordBuilder<T> recordBuilder = original.encode(style, ops, builder);
                                        ImmutableList<Effect> effects = ((TAStyle) style).textanimator$getEffects();
                                        if (effects.isEmpty()) {
                                                return recordBuilder;
                                        }
                                        return recordBuilder.add(
                                                        "ta$effects",
                                                        ops.createList(effects.stream().map(effect -> ops.createString(effect.serialize()))));
                                }

                                @Override
                                public <T> Stream<T> keys(DynamicOps<T> ops) {
                                        return Stream.concat(original.keys(ops), Stream.of(ops.createString("ta$effects")));
                                }

                                @Override
                                public String toString() {
                                        return original.toString();
                                }
                        };
                }
        }
}
