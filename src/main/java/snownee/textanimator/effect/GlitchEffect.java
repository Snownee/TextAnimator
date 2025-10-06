package snownee.textanimator.effect;

import java.util.Random;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

public class GlitchEffect extends BaseEffect {
	private final float intensity;
	private final float frequency;
	private final float shiftChance;
	private final float flickerChance;

	public GlitchEffect(Params params) {
		super(params);
		this.intensity = (float) params.getDouble("intensity").orElse(1.0);
		this.frequency = (float) params.getDouble("f").orElse(2.5);
		this.shiftChance = (float) params.getDouble("shift").orElse(0.04);
		this.flickerChance = (float) params.getDouble("flicker").orElse(0.002);
	}

	@Override
	public void apply(EffectSettings settings) {
		double time = Util.getMillis() * 0.001 * frequency;
		int pulse = (int) (time) % 3;

		Random rand = new Random(settings.index + (long) (time * 1000));

		float jitterX = 0, jitterY = 0;
		if (pulse == 1 && rand.nextFloat() < intensity * 0.7) {
			jitterX = (rand.nextFloat() - 0.5f) * 2 * intensity * 4;
			jitterY = (rand.nextFloat() - 0.5f) * 2 * intensity * 2;
		}
//
//		settings.x += jitterX;
//		settings.y += jitterY;

		if (rand.nextFloat() < shiftChance * intensity) {
			float rShift = rand.nextBoolean() ? intensity * 0.3f : 0;
			float bShift = rand.nextBoolean() ? intensity * 0.3f : 0;
//			settings.r = Math.min(1.0f, settings.r + rShift);
//			settings.b = Math.min(1.0f, settings.b + bShift);
		}

		if (rand.nextFloat() < flickerChance * intensity) {
			settings.a *= rand.nextFloat() < 0.3 ? 0.0f : 0.3f;
		}

//		if (rand.nextFloat() < 0.05 * intensity) {
//			settings.codepoint = 'A' + rand.nextInt(26);
//		}

		time *= 2;
		Random random = new Random((long) (time) * 1000L * hashCode());
		if (random.nextFloat() < 0.15) {
			if (settings.isShadow) {
				settings.siblings.add(settings.copy());
				settings.x -= settings.shadowOffset;
				settings.y -= settings.shadowOffset;
			}
			float mask = 0.5f + (random.nextFloat() - 0.5f) * 0.5f;
			float offset = 0.5f + random.nextFloat() * 0.5f;
			if (random.nextBoolean()) {
				offset = -offset;
			}
			EffectSettings copy = settings.copy();
			copy.x += offset;
			copy.a *= Math.min(1f, 0.5f + random.nextFloat());
			if (copy.isShadow) {
				copy.y -= 1;
				copy.r = copy.r > 0.5f ? copy.r - 0.5f : copy.r + 0.5f;
				copy.g *= 0.2f;
				copy.b *= 0.2f;
			}
			copy.maskBottom = mask;
			settings.siblings.add(copy);

			settings.maskTop = 1 - mask;
			settings.x -= offset;
			settings.a *= Math.min(1f, 0.5f + random.nextFloat());
			if (settings.isShadow) {
				settings.y += 1;
				settings.r *= 0.2f;
				settings.g = settings.g > 0.5f ? settings.g - 0.5f : settings.g + 0.5f;
				settings.b = settings.b > 0.5f ? settings.b - 0.5f : settings.b + 0.5f;
			}
		}
	}

	@Override
	public String getName() {
		return "glitch";
	}
}