package snownee.textanimator;

import snownee.textanimator.effect.BlurEffect;
import snownee.textanimator.effect.BounceEffect;
import snownee.textanimator.effect.EffectFactory;
import snownee.textanimator.effect.FadeEffect;
import snownee.textanimator.effect.GlitchEffect;
import snownee.textanimator.effect.GradientEffect;
import snownee.textanimator.effect.PulseColorEffect;
import snownee.textanimator.effect.RainbowEffect;
import snownee.textanimator.effect.PendulumEffect;
import snownee.textanimator.effect.ScrollEffect;
import snownee.textanimator.effect.ShadowEffect;
import snownee.textanimator.effect.ShadowOffEffect;
import snownee.textanimator.effect.ShakeEffect;
import snownee.textanimator.effect.SwingEffect;
import snownee.textanimator.effect.TurbulenceEffect;
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
		EffectFactory.register("pulse", PulseColorEffect::new);
		EffectFactory.register("bounce", BounceEffect::new);
		EffectFactory.register("swing", SwingEffect::new);
		EffectFactory.register("shadow", ShadowEffect::new);
		EffectFactory.register("fade", FadeEffect::new);
		EffectFactory.register("pend", PendulumEffect::new);
		EffectFactory.register("scroll", ScrollEffect::new);
		EffectFactory.register("turbulence", TurbulenceEffect::new);
		EffectFactory.register("glitch", GlitchEffect::new);
		EffectFactory.register("blur", BlurEffect::new);
		EffectFactory.register("grad", GradientEffect::new);
	}
}
