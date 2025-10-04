package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class BounceEffect implements Effect {
    private final float amp;
    private final float speed;
    private final float phase;

    public BounceEffect(Params params) {
        this.amp = (float) params.getDouble("amp").orElse(3.0);
        this.speed = (float) params.getDouble("speed").orElse(1.4);
        this.phase = (float) params.getDouble("phase").orElse(0.0);
    }

    @Override
    public void apply(EffectSettings settings) {
        float t = (float) Util.getMillis() * 0.002f * speed + settings.index * (0.6f + phase);
        settings.y += Math.abs(Mth.sin(t)) * amp;
    }

    @Override
    public String getName() { return "bounce"; }
}