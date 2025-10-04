package snownee.textanimator;

import snownee.textanimator.effect.*;
import snownee.textanimator.typewriter.TypewriterEffect;

public class TextAnimator {
    public static void init() {
        EffectFactory.register("typewriter", TypewriterEffect::new);
        EffectFactory.register("shake", ShakeEffect::new);
        EffectFactory.register("wave", WaveEffect::new);
        EffectFactory.register("rainb", RainbowEffect::new);
        EffectFactory.register("wiggle", WiggleEffect::new);
        EffectFactory.register("shadow-off", ShadowOffEffect::new);
        EffectFactory.register("gradient", GradientEffect::new);
        EffectFactory.register("pulse", PulseColorEffect::new);
        EffectFactory.register("bounce", BounceEffect::new);
        EffectFactory.register("swing", SwingEffect::new);
        EffectFactory.register("shadow", ShadowEffect::new);
        EffectFactory.register("fade", FadeEffect::new);
        EffectFactory.register("rotate", RotateEffect::new);
        EffectFactory.register("scroll", ScrollEffect::new);
        EffectFactory.register("turbulence", TurbulenceEffect::new);
        EffectFactory.register("glitch", GlitchEffect::new);
        EffectFactory.register("blur", BlurEffect::new);
    }
}
