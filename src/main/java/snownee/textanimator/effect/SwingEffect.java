package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class SwingEffect extends BaseEffect {
	private final float amp;
	private final float speed;
	private final float phase;

	public SwingEffect(Params params) {
		super(params);
		this.amp = (float) params.getDouble("a").orElse(1.0);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.phase = (float) params.getDouble("w").orElse(0.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		float t = (float) Util.getMillis() * 0.003f * speed + settings.index * phase;
		settings.rot += Mth.sin(t) * amp * 0.5f;
	}

	@Override
	public String getName() {
		return "swing";
	}
}