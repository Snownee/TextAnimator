package snownee.textanimator.effect;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

public class GradientEffect extends BaseEffect {
	private static final float[] defaultFrom = parseColor("5BCEFA").orElseThrow();
	private static final float[] defaultTo = parseColor("F5A9B8").orElseThrow();

	private final float[] fromRGB;
	private final float[] toRGB;
	private final boolean useHSV;
	private final float speed;
	private final float span;
	private final boolean cyclic;

	public GradientEffect(Params params) {
		super(params);
		this.fromRGB = parseColor(params, "from", defaultFrom);
		this.toRGB = parseColor(params, "to", defaultTo);
		this.useHSV = params.getBoolOr("hue", false);
		this.speed = (float) params.getDouble("f").orElse(0.0);
		this.span = (float) params.getDouble("sp").orElse(20.0);
		this.cyclic = !params.getBoolOr("uni", false);
	}

	@Override
	public void apply(EffectSettings settings) {
		if (settings.isShadow) {
			return;
		}

		float tIndex = span > 0 ? (settings.index % span) / span : 0;
		float tTime = speed > 0 ? (float) ((Util.getMillis() * 0.001) * speed % 1.0) : 0;
		float t = (tIndex + tTime) % 1;
		if (cyclic) {
			t = (t * 2) % 2;
			if (t > 1) {
				t = 2 - t;
			}
		}

		float[] rgb;
		if (useHSV) {
			float[] hsv1 = rgbToHsv(fromRGB);
			float[] hsv2 = rgbToHsv(toRGB);
			float h = lerpHue(hsv1[0], hsv2[0], t);
			float s = lerp(hsv1[1], hsv2[1], t);
			float v = lerp(hsv1[2], hsv2[2], t);
			rgb = hsvToRgb(h, s, v);
		} else {
			rgb = new float[]{lerp(fromRGB[0], toRGB[0], t), lerp(fromRGB[1], toRGB[1], t), lerp(fromRGB[2], toRGB[2], t)};
		}

		settings.r = rgb[0];
		settings.g = rgb[1];
		settings.b = rgb[2];
	}

	@Override
	public String getName() {
		return "grad";
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
