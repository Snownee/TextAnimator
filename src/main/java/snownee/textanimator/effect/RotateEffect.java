package snownee.textanimator.effect;

import snownee.textanimator.effect.params.Params;

public class RotateEffect implements Effect {
    private final float speed;
    private final float range;
    private final float radius;

    public RotateEffect(Params params) {
        this.speed = (float) params.getDouble("speed").orElse(1.0);
        this.range = (float) params.getDouble("range").orElse(0.4);
        this.radius = (float) params.getDouble("radius").orElse(1.5);
    }

    @Override
    public void apply(EffectSettings settings) {
        long t = net.minecraft.Util.getMillis();
        double phase = t * 0.002 * speed + settings.index * 0.1;
        settings.rotationRad = (float) (Math.sin(phase) * range);
        if (radius != 0) {
            settings.x += (float) (Math.cos(phase) * radius);
            settings.y += (float) (Math.sin(phase) * radius);
        }
    }

    @Override
    public String getName() {
        return "rotate";
    }
}