package snownee.textanimator.effect;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

import java.util.Random;

public class GlitchEffect implements Effect {
    private final float intensity;
    private final float frequency;
    private final float shiftChance;
    private final float flickerChance;

    public GlitchEffect(Params params) {
        this.intensity = (float) params.getDouble("intensity").orElse(1.0);
        this.frequency = (float) params.getDouble("freq").orElse(2.5);
        this.shiftChance = (float) params.getDouble("shift").orElse(0.4);
        this.flickerChance = (float) params.getDouble("flicker").orElse(0.1);
    }

    @Override
    public void apply(EffectSettings settings) {
        if (settings.isShadow) return;

        double time = Util.getMillis() * 0.001 * frequency;
        int pulse = (int) (time) % 3;

        Random rand = new Random(settings.index + (long)(time * 1000));

        float jitterX = 0, jitterY = 0;
        if (pulse == 1 && rand.nextFloat() < intensity * 0.7) {
            jitterX = (rand.nextFloat() - 0.5f) * 2 * intensity * 4;
            jitterY = (rand.nextFloat() - 0.5f) * 2 * intensity * 2;
        }

        settings.x += jitterX;
        settings.y += jitterY;

        if (rand.nextFloat() < shiftChance * intensity) {
            float rShift = rand.nextBoolean() ? intensity * 0.3f : 0;
            float bShift = rand.nextBoolean() ? intensity * 0.3f : 0;
            settings.r = Math.min(1.0f, settings.r + rShift);
            settings.b = Math.min(1.0f, settings.b + bShift);
        }

        if (rand.nextFloat() < flickerChance * intensity) {
            settings.a *= rand.nextFloat() < 0.3 ? 0.0f : 0.3f;
        }

        if (rand.nextFloat() < 0.05 * intensity) {
            settings.codepoint = 'A' + rand.nextInt(26);
        }
    }

    @Override
    public String getName() {
        return "glitch";
    }
}