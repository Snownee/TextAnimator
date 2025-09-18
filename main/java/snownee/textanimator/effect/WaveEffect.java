package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class WaveEffect implements Effect {
	public WaveEffect(Params params) {
	}

	@Override
	public void apply(EffectSettings settings) {
		settings.y += Mth.sin(Util.getMillis() * 0.01F + settings.index) * 2;
	}

	@Override
	public String getName() {
		return "wave";
	}
}
