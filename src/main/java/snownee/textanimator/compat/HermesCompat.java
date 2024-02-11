package snownee.textanimator.compat;

import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.google.common.collect.ImmutableList;

import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.TagElementSerializer;
import earth.terrarium.hermes.api.text.StyledTagElement;
import earth.terrarium.hermes.api.text.TextTagProvider;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.EffectFactory;
import snownee.textanimator.effect.params.EmptyParams;
import snownee.textanimator.effect.params.Params;
import snownee.textanimator.effect.params.StringPairParams;
import snownee.textanimator.typewriter.TypewriterTracks;
import snownee.textanimator.util.CommonProxy;

public class HermesCompat {

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		TextTagProvider.INSTANCE.addSerializer(type, new AdditionSerializer(type, factory));
		TextTagProvider.INSTANCE.addSerializer("!" + type, new RemovalSerializer(type));
	}

	public record AdditionSerializer(String type, Function<Params, Effect> factory) implements TagElementSerializer {
		@Override
		public TagElement deserialize(Map<String, String> parameters) {
			Params params = EmptyParams.INSTANCE;
			if (!parameters.isEmpty()) {
				params = new StringPairParams(parameters);
			}
			Effect effect = EffectFactory.create(type, params);
			if (effect == null) {
				return new StyledTagElement(UnaryOperator.identity());
			}
			return new StyledTagElement(style -> {
				style = CommonProxy.clone(style);
				TAStyle taStyle = (TAStyle) style;
				taStyle.textanimator$addEffect(effect);
				if ("typewriter".equals(type)) {
					taStyle.textanimator$setTypewriterTrack(TypewriterTracks.getInstance().get(new Object()));
				}
				return style;
			});
		}
	}

	public record RemovalSerializer(String type) implements TagElementSerializer {
		@Override
		public TagElement deserialize(Map<String, String> parameters) {
			return new StyledTagElement(style -> {
				style = CommonProxy.clone(style);
				TAStyle taStyle = (TAStyle) style;
				ImmutableList<Effect> effects = taStyle.textanimator$getEffects();
				if (!effects.isEmpty() && effects.get(effects.size() - 1).getName().equals(type)) {
					taStyle.textanimator$setEffects(effects.subList(0, effects.size() - 1));
					if ("typewriter".equals(type)) {
						taStyle.textanimator$setTypewriterTrack(null);
					}
				}
				return style;
			});
		}
	}

}
