package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class WaveEffect extends BaseEffect {
	private final float amp, speed, phase;

	public WaveEffect(Params params) {
		super(params);
		this.amp = (float) params.getDouble("a").orElse(1.0);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.phase = (float) params.getDouble("w").orElse(1.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		settings.y += Mth.sin(Util.getMillis() * 0.01F * speed + settings.index * phase) * 2 * amp;
	}

	@Override
	public String getName() {
		return "wave";
	}
}
