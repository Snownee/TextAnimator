package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class PulseColorEffect extends BaseEffect {
	private final float base;
	private final float amp;
	private final float speed;
	private final float phase;

	public PulseColorEffect(Params params) {
		super(params);
		this.base = (float) params.getDouble("base").orElse(0.75);
		this.amp = (float) params.getDouble("a").orElse(1.0);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.phase = (float) params.getDouble("w").orElse(0.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		if (settings.isShadow) {
			return;
		}
		float t = (float) Util.getMillis() * 0.002f * speed + settings.index * phase;
		float k = base + amp * (2f + 2f * Mth.sin(t));
		settings.r *= k;
		settings.g *= k;
		settings.b *= k;
	}

	@Override
	public String getName() {
		return "pulse";
	}
}