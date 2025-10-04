package snownee.textanimator.effect;

import java.util.Optional;
import java.util.OptionalDouble;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

public class GradientEffect implements Effect {
    private final float[] start;
    private final float[] end;
    private final float span;
    private final float speed;

    public GradientEffect(Params params) {
        this.start = parseColor(params, "start", new float[]{1f, 0.6f, 0.2f});
        this.end = parseColor(params, "end", new float[]{0.2f, 0.6f, 1f});
        OptionalDouble spanOpt = params.getDouble("span");
        this.span = (float) (spanOpt.isPresent() ? Math.max(1, spanOpt.getAsDouble()) : 12.0);
        OptionalDouble speedOpt = params.getDouble("speed");
        this.speed = (float) (speedOpt.isPresent() ? speedOpt.getAsDouble() : 0.0);
    }

    @Override
    public void apply(EffectSettings settings) {
        if (settings.isShadow) return;
        float tIndex = (settings.index % span) / span;
        float tTime = speed == 0 ? 0 : (float) ((Util.getMillis() * 0.001) * speed % 1.0);
        float t = clamp01(tIndex + tTime);
        settings.r = lerp(start[0], end[0], t);
        settings.g = lerp(start[1], end[1], t);
        settings.b = lerp(start[2], end[2], t);
    }

    @Override
    public String getName() {
        return "gradient";
    }

    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float clamp01(float v) { return v < 0 ? 0 : (v > 1 ? 1 : v); }

    private static float[] parseColor(Params params, String key, float[] def) {
        Optional<String> hex = params.getString(key);
        if (hex.isPresent()) {
            String s = hex.get().trim();
            if (s.startsWith("#")) s = s.substring(1);
            try {
                int val = (int) Long.parseLong(s, 16);
                float r = ((val >> 16) & 0xFF) / 255f;
                float g = ((val >> 8) & 0xFF) / 255f;
                float b = (val & 0xFF) / 255f;
                return new float[]{r, g, b};
            } catch (Exception ignored) {}
        }
        float r = (float) params.getDouble(key + "R").orElse(def[0]);
        float g = (float) params.getDouble(key + "G").orElse(def[1]);
        float b = (float) params.getDouble(key + "B").orElse(def[2]);
        return new float[]{r, g, b};
    }
}