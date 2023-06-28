package snownee.textanimator.effect;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;

import net.minecraft.util.RandomSource;

public interface Effect {
	RandomSource RANDOM = RandomSource.create();

	static Effect create(String[] split) {
		JsonObject params = null;
		if (split.length > 1) {
			params = new JsonObject();
			for (int i = 1; i < split.length; i++) {
				String[] kv = StringUtils.split(split[i], "=", 2);
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
			case "shake" -> new ShakeEffect(params);
			case "wave" -> new WaveEffect(params);
			case "rainb" -> new RainbowEffect(params);
			case "wiggle" -> new WiggleEffect(params);
			default -> null;
		};
	}

	void apply(EffectSettings settings);

	String getName();
}
