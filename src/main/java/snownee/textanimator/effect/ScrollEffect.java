package snownee.textanimator.effect;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

public class ScrollEffect implements Effect {
    private final float speed;

    public ScrollEffect(Params params) {
        this.speed = (float) params.getDouble("speed").orElse(1.0);
    }

    @Override
    public void apply(EffectSettings settings) {
        float period = 40f;
        float offset = (Util.getMillis() * 0.04f * speed) % period;
        settings.x -= offset;
    }

    @Override
    public String getName() {
        return "scroll";
    }
}