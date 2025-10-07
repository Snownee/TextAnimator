package snownee.textanimator.effect;

import java.util.Random;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

public class GlitchEffect extends BaseEffect {
	private final float frequency;
	private final float jitterChance;
	private final float blinkChance;
	private final float shiftChance;

	public GlitchEffect(Params params) {
		super(params);
		this.frequency = (float) params.getDouble("f").orElse(1.0);
		this.jitterChance = (float) params.getDouble("j").orElse(0.015);
		this.blinkChance = (float) params.getDouble("b").orElse(0.003);
		this.shiftChance = (float) params.getDouble("s").orElse(0.08);
	}

	@Override
	public void apply(EffectSettings settings) {
		double time = Util.getMillis() * 0.025 * frequency;
		int pulse = (int) (time) % 3;

		Random random = new Random(settings.index + settings.codepoint + (long) (time * 1000));
		random.nextFloat(); // skip one

		if (pulse == 1 && random.nextFloat() < jitterChance) {
			settings.x += (random.nextFloat() - 0.5f) * 8;
			settings.y += (random.nextFloat() - 0.5f) * 4;
		}

//		if (random.nextFloat() < shiftChance * intensity) {
//			float rShift = random.nextBoolean() ? intensity * 0.3f : 0;
//			float bShift = random.nextBoolean() ? intensity * 0.3f : 0;
//			settings.r = Math.min(1.0f, settings.r + rShift);
//			settings.b = Math.min(1.0f, settings.b + bShift);
//		}

		if (random.nextFloat() < blinkChance) {
			settings.a *= random.nextFloat() < 0.3 ? 0.0f : 0.3f;
		}

//		if (random.nextFloat() < 0.05 * intensity) {
//			settings.codepoint = 'A' + random.nextInt(26);
//		}

		time *= 2;
		Random random2 = new Random((long) (time) * 1000L * hashCode());
		if (random2.nextFloat() < shiftChance) {
			if (settings.isShadow) {
				settings.siblings.add(settings.copy());
				settings.x -= settings.shadowOffset;
				settings.y -= settings.shadowOffset;
			}
			float mask = 0.5f + (random2.nextFloat() - 0.5f) * 0.5f;
			float offset = 0.75f + random2.nextFloat() * 0.75f;
			if (random2.nextBoolean()) {
				offset = -offset;
			}
			EffectSettings copy = settings.copy();
			copy.x += offset;
			copy.a *= Math.min(1f, 0.5f + random2.nextFloat());
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
			settings.a *= Math.min(1f, 0.5f + random2.nextFloat());
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