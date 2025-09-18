package snownee.textanimator.effect;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import snownee.textanimator.effect.params.Params;
import snownee.textanimator.util.CommonProxy;

import java.util.Map;
import java.util.function.Function;

public class EffectFactory {
	private static final Map<String, Function<Params, Effect>> factories = Maps.newHashMap();

	@NotNull
	public static Effect create(String type, Params params) {
		Function<Params, Effect> factory = factories.get(type);
		if (factory == null) {
			throw new IllegalArgumentException("Unknown effect type: " + type);
		}
		return factory.apply(params);
	}

	public static synchronized void register(String type, Function<Params, Effect> factory) {
		factories.put(type, factory);
		CommonProxy.onEffectTypeRegistered(type, factory);
	}
}
