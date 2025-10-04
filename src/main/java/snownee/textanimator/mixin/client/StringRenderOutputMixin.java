package snownee.textanimator.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import snownee.textanimator.TextAnimationMode;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.*;
import snownee.textanimator.effect.EffectSettings;
import snownee.textanimator.typewriter.TypewriterTrack;

import java.util.function.Function;

@Mixin(value = Font.StringRenderOutput.class, priority = 1200)
public abstract class StringRenderOutputMixin {

	@Shadow
	@Final
	private Matrix4f pose;

	@Shadow
	@Final
	private MultiBufferSource bufferSource;

	@Shadow
	float x;

	@Shadow
	float y;

	@Shadow
	@Final
	private Font this$0;

	@Shadow
	@Final
	private boolean dropShadow;

	@Shadow
	@Final
	private float dimFactor;

	@Shadow
	@Final
	private float a;

	@Shadow
	@Final
	private float b;

	@Shadow
	@Final
	private float r;

	@Shadow
	@Final
	private float g;

	@Shadow
	@Final
	private Font.DisplayMode mode;

	@Shadow
	@Final
	private int packedLightCoords;

	@Shadow
	protected abstract void addEffect(BakedGlyph.Effect effect);

	@Inject(method = "accept", at = @At("HEAD"), cancellable = true)
	private void textanimator$accept(int index, Style style, int codepoint, CallbackInfoReturnable<Boolean> cir) {
		TAStyle taStyle = (TAStyle) style;

		if (taStyle.textanimator$getEffects().isEmpty()) {
			return;
		}

		FontSet fontSet = ((FontAccess) this$0).callGetFontSet(style.getFont());
		GlyphInfo glyphInfo = fontSet.getGlyphInfo(codepoint, ((FontAccess) this$0).getFilterFishyGlyphs());
		BakedGlyph bakedGlyph = style.isObfuscated() && codepoint != 32
				? fontSet.getRandomGlyph(glyphInfo)
				: fontSet.getGlyph(codepoint);

		boolean bold = style.isBold();
		float shadowOffset = dropShadow ? glyphInfo.getShadowOffset() : 0.0f;
		float alpha = this.a;

		float red, green, blue;
		TextColor textColor = style.getColor();
		if (textColor != null) {
			int colorValue = textColor.getValue();
			red = (float) ((colorValue >> 16) & 0xFF) / 255.0f * dimFactor;
			green = (float) ((colorValue >> 8) & 0xFF) / 255.0f * dimFactor;
			blue = (float) (colorValue & 0xFF) / 255.0f * dimFactor;
		} else {
			red = this.r;
			green = this.g;
			blue = this.b;
		}

		if (bakedGlyph instanceof EmptyGlyph) {
			x += glyphInfo.getAdvance(bold);
			cir.setReturnValue(true);
			return;
		}

		float boldOffset = bold ? glyphInfo.getBoldOffset() : 0.0f;
		TypewriterTrack typewriterTrack = taStyle.textanimator$getTypewriterTrack();
		int typingIndex = taStyle.textanimator$getTypewriterIndex();

		EffectSettings settings = new EffectSettings(codepoint, index + Math.max(typingIndex, 0), dropShadow, typewriterTrack, typingIndex);
		settings.x = x + shadowOffset;
		settings.y = y + shadowOffset;
		settings.r = red;
		settings.g = green;
		settings.b = blue;
		settings.a = alpha;

		TextAnimationMode animationMode = TextAnimatorClient.getTextAnimationMode();
		for (int i = taStyle.textanimator$getEffects().size() - 1; i >= 0; i--) {
			Effect effect = taStyle.textanimator$getEffects().get(i);
			if (animationMode.shouldApply(effect)) {
				effect.apply(settings);
			}
		}

		red = settings.r;
		green = settings.g;
		blue = settings.b;
		alpha = settings.a;

		if (alpha > 0) {
			VertexConsumer vertexConsumer = bufferSource.getBuffer(bakedGlyph.renderType(mode));
			Matrix4f poseMain = pose;

			if (settings.rotationRad != 0) {
				Matrix4f matrix = new Matrix4f(pose);
				matrix.translate(settings.x, settings.y, 0);
				matrix.rotateZ(settings.rotationRad);
				matrix.translate(-settings.x, -settings.y, 0);
				poseMain = matrix;
			}

			((FontAccess) this$0).callRenderChar(
					bakedGlyph, bold, style.isItalic(), boldOffset,
					settings.x, settings.y, poseMain, vertexConsumer,
					red, green, blue, alpha, packedLightCoords);

			for (Effect effect : taStyle.textanimator$getEffects()) {
				if (!animationMode.shouldApply(effect)) continue;

				if (effect instanceof BlurEffect) {
					renderBlurEffect((BlurEffect) effect, bakedGlyph, bold, style.isItalic(), boldOffset, settings, poseMain, vertexConsumer, red, green, blue, alpha);
				}
			}

			if (style.isStrikethrough()) {
				addEffect(new BakedGlyph.Effect(
						x + shadowOffset - 1.0f,
						y + shadowOffset + 4.5f,
						x + shadowOffset + glyphInfo.getAdvance(bold),
						y + shadowOffset + 4.5f - 1.0f,
						0.01f, red, green, blue, alpha));
			}

			if (style.isUnderlined()) {
				addEffect(new BakedGlyph.Effect(
						x + shadowOffset - 1.0f,
						y + shadowOffset + 9.0f,
						x + shadowOffset + glyphInfo.getAdvance(bold),
						y + shadowOffset + 9.0f - 1.0f,
						0.01f, red, green, blue, alpha));
			}
		}

		x += glyphInfo.getAdvance(bold);
		cir.setReturnValue(true);
	}

	private void renderBlurEffect(BlurEffect blurEffect, BakedGlyph bakedGlyph, boolean bold, boolean italic,
			float boldOffset, EffectSettings settings, Matrix4f pose,
			VertexConsumer vertexConsumer, float r, float g, float b, float a) {
		int passes = Math.max(4, blurEffect.passes);
		float radius = blurEffect.radius;
		float alphaMul = blurEffect.alphaMul;

		renderRadialPasses(bakedGlyph, bold, italic, boldOffset, settings, pose, vertexConsumer,
				r, g, b, a, passes, radius, alphaMul, colors -> colors);
	}

	private void renderRadialPasses(
			BakedGlyph bakedGlyph,
			boolean bold, boolean italic, float boldOffset,
			EffectSettings settings,
			Matrix4f pose,
			VertexConsumer vertexConsumer,
			float baseR, float baseG, float baseB, float baseA,
			int passes,
			float radius,
			float alphaMul,
			Function<float[], float[]> colorTransformer) {

		for (int i = 0; i < passes; i++) {
			float angle = (float) (Math.PI * 2 * i / passes);
			float dx = (float) Math.cos(angle) * radius;
			float dy = (float) Math.sin(angle) * radius;
			float alpha = baseA * alphaMul;
			if (alpha <= 0) continue;

			float[] color = colorTransformer.apply(new float[]{baseR, baseG, baseB, alpha});

			((FontAccess) this$0).callRenderChar(
					bakedGlyph, bold, italic, boldOffset,
					settings.x + dx, settings.y + dy, pose, vertexConsumer,
					color[0], color[1], color[2], color[3], packedLightCoords);
		}
	}
}