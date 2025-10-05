package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class TurbulenceEffect implements Effect {
	private final float amp;
	private final float speed;

	public TurbulenceEffect(Params params) {
		this.amp = (float) params.getDouble("amp").orElse(1.5);
		this.speed = (float) params.getDouble("speed").orElse(1.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		float t = Util.getMillis() * 0.002f * speed;
		float nx = Mth.sin(t * 1.7f + settings.index * 0.31f + settings.codepoint * 0.07f);
		float ny = Mth.sin(t * 2.3f + settings.index * 0.27f + settings.codepoint * 0.11f);
		settings.x += nx * amp;
		settings.y += ny * amp;
	}

	@Override
	public String getName() {return "turb";}
}