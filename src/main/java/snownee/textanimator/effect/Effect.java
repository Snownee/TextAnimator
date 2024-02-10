package snownee.textanimator.effect;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;

import snownee.textanimator.typewriter.TypewriterEffect;

public interface Effect {

	static Effect create(String[] split, boolean allowTypewriter) {
		JsonObject params = null;
		if (split.length > 1) {
			params = new JsonObject();
			for (int i = 1; i < split.length; i++) {
				String[] kv = StringUtils.split(split[i], "=", 2);
				if (kv.length == 0) continue;
				if (kv.length == 1) {
					params.addProperty(kv[0], true);
				} else if ("true".equals(kv[1]) || "false".equals(kv[1])) {
					params.addProperty(kv[0], Boolean.parseBoolean(kv[1]));
				} else {
					try {
						params.addProperty(kv[0], Float.parseFloat(kv[1]));
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Invalid effect parameter: " + kv[0] + "=" + kv[1]);
					}
				}
			}
		}
		return switch (split[0]) {
			case "typewriter" -> allowTypewriter ? new TypewriterEffect(params) : null;
			case "shake" -> new ShakeEffect(params);
			case "wave" -> new WaveEffect(params);
			case "rainb" -> new RainbowEffect(params);
			case "wiggle" -> new WiggleEffect(params);
			default -> null;
		};
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
