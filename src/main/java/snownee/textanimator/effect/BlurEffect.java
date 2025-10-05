package snownee.textanimator.effect;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.joml.Matrix4f;
import snownee.textanimator.effect.params.Params;
import snownee.textanimator.mixin.client.FontAccess;

public class BlurEffect implements Effect {
    public final int passes;
    public final float radius;
    public final float alphaMul;

    public BlurEffect(Params params) {
        this.passes = (int) Math.max(4, params.getDouble("passes").orElse(10));
        this.radius = (float) params.getDouble("radius").orElse(2);
        this.alphaMul = (float) params.getDouble("alpha").orElse(0.12);
    }

    @Override
    public void apply(EffectSettings settings) {
    }

    @Override
    public String getName() {
        return "blur";
    }

    public void render(
            BakedGlyph bakedGlyph,
            boolean bold, boolean italic, float boldOffset,
            EffectSettings settings,
            Matrix4f pose,
            VertexConsumer vertexConsumer,
            float baseR, float baseG, float baseB, float baseA,
            int packedLightCoords) {

        float alpha = baseA * alphaMul;
        if (alpha <= 0) return;

        for (int i = 0; i < passes; i++) {
            float angle = (float) (Math.PI * 2 * i / passes);
            float dx = (float) Math.cos(angle) * radius;
            float dy = (float) Math.sin(angle) * radius;

            float x = settings.x + dx;
            float y = settings.y + dy;

            ((FontAccess) Minecraft.getInstance().font).callRenderChar(
                    bakedGlyph, bold, italic, boldOffset,
                    x, y, pose, vertexConsumer,
                    baseR, baseG, baseB, alpha, packedLightCoords
            );
        }
    }
}