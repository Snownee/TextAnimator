package snownee.textanimator;

import snownee.textanimator.effect.BounceEffect;
import snownee.textanimator.effect.EffectFactory;
import snownee.textanimator.effect.FadeEffect;
import snownee.textanimator.effect.GlitchEffect;
import snownee.textanimator.effect.GradientEffect;
import snownee.textanimator.effect.NeonEffect;
import snownee.textanimator.effect.PendulumEffect;
import snownee.textanimator.effect.PulseColorEffect;
import snownee.textanimator.effect.RainbowEffect;
import snownee.textanimator.effect.ScrollEffect;
import snownee.textanimator.effect.ShadowEffect;
import snownee.textanimator.effect.ShakeEffect;
import snownee.textanimator.effect.SwingEffect;
import snownee.textanimator.effect.TurbulenceEffect;
import snownee.textanimator.effect.WaveEffect;
import snownee.textanimator.effect.WiggleEffect;
import snownee.textanimator.typewriter.TypewriterEffect;

public class TextAnimator {
	public static void init() {
		EffectFactory.register(TypewriterEffect::new);
		EffectFactory.register(ShakeEffect::new);
		EffectFactory.register(WaveEffect::new);
		EffectFactory.register(RainbowEffect::new);
		EffectFactory.register(WiggleEffect::new);
		EffectFactory.register(PulseColorEffect::new);
		EffectFactory.register(BounceEffect::new);
		EffectFactory.register(SwingEffect::new);
		EffectFactory.register(ShadowEffect::new);
		EffectFactory.register(FadeEffect::new);
		EffectFactory.register(PendulumEffect::new);
//		EffectFactory.register(ScrollEffect::new);
		EffectFactory.register(TurbulenceEffect::new);
		EffectFactory.register(GlitchEffect::new);
		EffectFactory.register(NeonEffect::new);
		EffectFactory.register(GradientEffect::new);
	}
}
