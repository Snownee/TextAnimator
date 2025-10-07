package snownee.textanimator.effect;

import java.util.Optional;

import snownee.textanimator.effect.params.Params;

public abstract class BaseEffect implements Effect {
	protected final Params params;

	public BaseEffect(Params params) {
		this.params = params;
	}

	public static float[] parseColor(Params params, String key, float[] def) {
		return params.getString(key).flatMap(BaseEffect::parseColor).orElse(def);
	}

	public static Optional<float[]> parseColor(String s) {
		s = s.trim();
		if (s.startsWith("#")) {
			s = s.substring(1);
		}
		try {
			int val = (int) Long.parseLong(s, 16);
			return Optional.of(new float[]{
					((val >> 16) & 0xFF) / 255f, ((val >> 8) & 0xFF) / 255f, (val & 0xFF) / 255f});
		} catch (Exception ignored) {
			return Optional.empty();
		}
	}

	@Override
	public String serialize() {
		return getName() + params.serialize();
	}
}
