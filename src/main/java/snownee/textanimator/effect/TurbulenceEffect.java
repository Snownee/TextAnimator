package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class TurbulenceEffect extends BaseEffect {
	private final float amp;
	private final float speed;

	public TurbulenceEffect(Params params) {
		super(params);
		this.amp = (float) params.getDouble("a").orElse(1.0);
		this.speed = (float) params.getDouble("f").orElse(1.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		float amp = this.amp * 1.5f;
		float t = Util.getMillis() * 0.002f * speed;
		float nx = Mth.sin(t * 1.7f + settings.index * 0.31f + settings.codepoint * 0.07f);
		float ny = Mth.sin(t * 2.3f + settings.index * 0.27f + settings.codepoint * 0.11f);
		settings.x += nx * amp;
		settings.y += ny * amp;
	}

	@Override
	public String getName() {return "turb";}
}