package snownee.textanimator.effect;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

public class GradientEffect implements Effect {
	private final float[] startRGB;
	private final float[] endRGB;
	private final boolean useHSV;
	private final float speed;
	private final float span;

	public GradientEffect(Params params) {
		this.startRGB = parseColor(params, "start", new float[]{1f, 0.6f, 0.2f});
		this.endRGB = parseColor(params, "end", new float[]{0.2f, 0.6f, 1f});
		this.useHSV = params.getBoolOr("hsv", false);
		this.speed = (float) params.getDouble("speed").orElse(0.0);
		this.span = (float) params.getDouble("span").orElse(30.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		if (settings.isShadow) {
			return;
		}

		float tIndex = (settings.index % span) / span;
		float tTime = speed > 0 ? (float) ((Util.getMillis() * 0.001) * speed % 1.0) : 0;
		float t = (tIndex + tTime) % 1.0f;

		float[] rgb;
		if (useHSV) {
			float[] hsv1 = rgbToHsv(startRGB);
			float[] hsv2 = rgbToHsv(endRGB);
			float h = lerpHue(hsv1[0], hsv2[0], t);
			float s = lerp(hsv1[1], hsv2[1], t);
			float v = lerp(hsv1[2], hsv2[2], t);
			rgb = hsvToRgb(h, s, v);
		} else {
			rgb = new float[]{
					lerp(startRGB[0], endRGB[0], t),
					lerp(startRGB[1], endRGB[1], t),
					lerp(startRGB[2], endRGB[2], t)
			};
		}

		settings.r = rgb[0];
		settings.g = rgb[1];
		settings.b = rgb[2];
	}

	@Override
	public String getName() {
		return "grad";
	}


	private static float[] parseColor(Params params, String key, float[] def) {
		return params.getString(key).map(s -> {
			s = s.trim();
			if (s.startsWith("#")) {
				s = s.substring(1);
			}
			try {
				int val = (int) Long.parseLong(s, 16);
				return new float[]{
						((val >> 16) & 0xFF) / 255f,
						((val >> 8) & 0xFF) / 255f,
						(val & 0xFF) / 255f
				};
			} catch (Exception ignored) {
			}
			return def;
		}).orElse(def);
	}

	private static float[] rgbToHsv(float[] rgb) {
		float r = rgb[0], g = rgb[1], b = rgb[2];
		float max = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.min(g, b));
		float h, s;
		float d = max - min;
		s = max == 0 ? 0 : d / max;

		if (d == 0) {
			h = 0;
		} else if (max == r) {
			h = (g - b) / d + (g < b ? 6 : 0);
		} else if (max == g) {
			h = (b - r) / d + 2;
		} else {
			h = (r - g) / d + 4;
		}
		h /= 6f;
		return new float[]{h, s, max};
	}

	private static float[] hsvToRgb(float h, float s, float v) {
		int i = (int) (h * 6);
		float f = h * 6 - i;
		float p = v * (1 - s);
		float q = v * (1 - f * s);
		float t = v * (1 - (1 - f) * s);
		return switch (i % 6) {
			case 0 -> new float[]{v, t, p};
			case 1 -> new float[]{q, v, p};
			case 2 -> new float[]{p, v, t};
			case 3 -> new float[]{p, q, v};
			case 4 -> new float[]{t, p, v};
			default -> new float[]{v, p, q};
		};
	}

	private static float lerpHue(float a, float b, float t) {
		float diff = (b - a + 1f) % 1f;
		if (diff > 0.5f) {
			diff -= 1f;
		}
		return (a + diff * t + 1f) % 1f;
	}

	private static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}
}
