package snownee.textanimator.effect;

import snownee.textanimator.effect.params.Params;

public class ShadowOffEffect implements Effect {
	public ShadowOffEffect(Params params) {
	}

	@Override
	public void apply(EffectSettings settings) {
		if (settings.isShadow) {
			settings.a = 0;
		}
	}

	@Override
	public String getName() {
		return "shadow-off";
	}
}
