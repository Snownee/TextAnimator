package snownee.textanimator;

import snownee.textanimator.effect.EffectFactory;
import snownee.textanimator.effect.RainbowEffect;
import snownee.textanimator.effect.ShadowOffEffect;
import snownee.textanimator.effect.ShakeEffect;
import snownee.textanimator.effect.WaveEffect;
import snownee.textanimator.effect.WiggleEffect;
import snownee.textanimator.typewriter.TypewriterEffect;

public class TextAnimator {
	public static void init() {
		EffectFactory.register("typewriter", TypewriterEffect::new);
		EffectFactory.register("shake", ShakeEffect::new);
		EffectFactory.register("wave", WaveEffect::new);
		EffectFactory.register("rainb", RainbowEffect::new);
		EffectFactory.register("wiggle", WiggleEffect::new);
		EffectFactory.register("shadow-off", ShadowOffEffect::new);
	}
}
