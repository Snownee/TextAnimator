package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class FadeEffect extends BaseEffect {
	private final float minA;
	private final float speed;
	private final float phase;

	public FadeEffect(Params params) {
		super(params);
		this.minA = (float) params.getDouble("a").orElse(0.3);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.phase = (float) params.getDouble("w").orElse(0.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		float t = (float) Util.getMillis() * 0.002f * speed + settings.index * phase;
		float k = minA + (1f - minA) * (0.5f + 0.5f * Mth.sin(t));
		settings.a *= k;
	}

	@Override
	public String getName() {
		return "fade";
	}
}