package snownee.textanimator.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import snownee.textanimator.duck.TABakedGlyph;
import snownee.textanimator.effect.EffectSettings;

@Mixin(BakedGlyph.class)
public class BakedGlyphMixin implements TABakedGlyph {
	@Shadow
	@Final
	private float left;
	@Shadow
	@Final
	private float right;
	@Shadow
	@Final
	private float up;
	@Shadow
	@Final
	private float down;
	@Shadow
	@Final
	private float u0;
	@Shadow
	@Final
	private float v0;
	@Shadow
	@Final
	private float u1;
	@Shadow
	@Final
	private float v1;

	@Override
	public void textanimator$render(
			EffectSettings settings,
			boolean italic,
			float xOffset,
			Matrix4f pose,
			VertexConsumer vertexConsumer,
			int packedLightCoords) {
		float x = settings.x + xOffset;
		float left = x + this.left;
		float right = x + this.right;
		float up = settings.y + this.up;
		float down = settings.y + this.down;
		float $$17 = italic ? 1.0F - 0.25F * this.up : 0.0F;
		float $$18 = italic ? 1.0F - 0.25F * this.down : 0.0F;
		float u0 = this.u0;
		float u1 = this.u1;
		float v0 = this.v0;
		float v1 = this.v1;
//		if (settings.maskLeft != 0) {
//			u0 += (this.u1 - this.u0) * settings.maskLeft;
//			left += (this.right - this.left) * settings.maskLeft;
//		}
//		if (settings.maskRight != 0) {
//			u1 -= (this.u1 - this.u0) * settings.maskRight;
//			right -= (this.right - this.left) * settings.maskRight;
//		}
		if (settings.maskTop != 0) {
			v0 += (this.v1 - this.v0) * settings.maskTop;
			up += (this.down - this.up) * settings.maskTop;
		}
		if (settings.maskBottom != 0) {
			v1 -= (this.v1 - this.v0) * settings.maskBottom;
			down -= (this.down - this.up) * settings.maskBottom;
		}
		vertexConsumer.addVertex(pose, left + $$17, up, 0.0F)
				.setColor(settings.r, settings.g, settings.b, settings.a)
				.setUv(u0, v0)
				.setLight(packedLightCoords);
		vertexConsumer.addVertex(pose, left + $$18, down, 0.0F)
				.setColor(settings.r, settings.g, settings.b, settings.a)
				.setUv(u0, v1)
				.setLight(packedLightCoords);
		vertexConsumer.addVertex(pose, right + $$18, down, 0.0F)
				.setColor(settings.r, settings.g, settings.b, settings.a)
				.setUv(u1, v1)
				.setLight(packedLightCoords);
		vertexConsumer.addVertex(pose, right + $$17, up, 0.0F)
				.setColor(settings.r, settings.g, settings.b, settings.a)
				.setUv(u1, v0)
				.setLight(packedLightCoords);
	}
}
