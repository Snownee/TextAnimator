package snownee.textanimator.effect;

import snownee.textanimator.effect.params.Params;

public class ShadowEffect implements Effect {
    private final float dx, dy, r, g, b, a;

    public ShadowEffect(Params params) {
        this.dx = (float) params.getDouble("dx").orElse(1.0);
        this.dy = (float) params.getDouble("dy").orElse(1.0);
        this.r = (float) params.getDouble("r").orElse(0.0);
        this.g = (float) params.getDouble("g").orElse(0.0);
        this.b = (float) params.getDouble("b").orElse(0.0);
        this.a = (float) params.getDouble("a").orElse(1.0);
    }

    @Override
    public void apply(EffectSettings settings) {
        if (!settings.isShadow) return;
        settings.x += dx;
        settings.y += dy;
        settings.r = r;
        settings.g = g;
        settings.b = b;
        settings.a *= a;
    }

    @Override
    public String getName() { return "shadow"; }
}