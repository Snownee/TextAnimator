package snownee.textanimator.effect;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

import snownee.textanimator.effect.params.EmptyParams;
import snownee.textanimator.effect.params.Params;
import snownee.textanimator.effect.params.TypedParams;

public interface Effect {

	static Effect create(String[] split, boolean allowTypewriter) {
		if (split.length == 0) {
			return null;
		}
		if (!allowTypewriter && "typewriter".equals(split[0])) {
			return null;
		}
		Params params = EmptyParams.INSTANCE;
		if (split.length > 1) {
			ImmutableMap.Builder<String, Object> builder = ImmutableMap.builderWithExpectedSize(split.length - 1);
			for (int i = 1; i < split.length; i++) {
				String[] kv = StringUtils.split(split[i], "=", 2);
				if (kv.length < 2) continue;
				if ("true".equals(kv[1]) || "false".equals(kv[1])) {
					builder.put(kv[0], Boolean.parseBoolean(kv[1]));
				} else {
					try {
						builder.put(kv[0], Double.parseDouble(kv[1]));
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Invalid effect parameter: " + kv[0] + "=" + kv[1]);
					}
				}
			}
			params = new TypedParams(builder.build());
		}
		return EffectFactory.create(split[0], params);
	}

	static Effect create(String tagContent, boolean allowTypewriter) {
		return create(StringUtils.split(tagContent, ' '), allowTypewriter);
	}

	void apply(EffectSettings settings);

	String getName();

	default String serialize() {
		return getName();
	}
}
