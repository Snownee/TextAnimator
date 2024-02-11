package snownee.textanimator.effect;

import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import snownee.textanimator.effect.params.Params;
import snownee.textanimator.util.CommonProxy;

public class EffectFactory {
	private static final Map<String, Function<Params, Effect>> factories = Maps.newHashMap();

	@Nullable
	public static Effect create(String type, Params params) {
		Function<Params, Effect> factory = factories.get(type);
		if (factory == null) {
			return null;
		}
		return factory.apply(params);
	}

	public static synchronized void register(String type, Function<Params, @Nullable Effect> factory) {
		factories.put(type, factory);
		CommonProxy.onEffectTypeRegistered(type, factory);
	}
}
