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
				} else {
					try {
						params.addProperty(kv[0], Integer.parseInt(kv[1]));
					} catch (NumberFormatException e) {
						params.addProperty(kv[0], kv[1]);
					}
				}
			}
		}
		switch (split[0]) {
			case "shake":
				return new ShakeEffect(params);
			case "wave":
				return new WaveEffect(params);
			default:
				return null;
		}
	}

	void apply(EffectSettings settings);

	String getName();
}
