package snownee.textanimator.mixin.client;

import java.util.function.Function;

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
import snownee.textanimator.effect.BlurEffect;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.EffectSettings;
import snownee.textanimator.typewriter.TypewriterTrack;

@Mixin(value = Font.StringRenderOutput.class, priority = 1200)
public abstract class StringRenderOutputMixin {
	@Shadow
	@Final
	private Matrix4f pose;
	@Final
	@Shadow
	MultiBufferSource bufferSource;
	@Shadow
	float x;
	@Shadow
	float y;
	@Final
	@Shadow(remap = false, aliases = {"f_92938_", "field_24240", "b"})
	Font this$0;
	@Final
	@Shadow
	private boolean dropShadow;
	@Final
	@Shadow
	private float dimFactor;
	@Final
	@Shadow
	private float a;
	@Final
	@Shadow
	private float b;
	@Final
	@Shadow
	private float r;
	@Final
	@Shadow
	private float g;
	@Final
	@Shadow
	private Font.DisplayMode mode;
	@Final
	@Shadow
	private int packedLightCoords;

	@Inject(method = "accept", at = @At("HEAD"), cancellable = true)
	private void textanimator$accept(int index, Style style, int codepoint, CallbackInfoReturnable<Boolean> cir) {
		// 绕过左侧编辑区的特效渲染
		if (snownee.textanimator.TextAnimatorClient.isEffectBypass()) {
			return; // 不取消，交由原版路径处理
		}
		TAStyle taStyle = (TAStyle) style;
		if (taStyle.textanimator$getEffects().isEmpty()) {
			return;
		}
		float b;
		float g;
		float r;
		FontSet fontSet = ((FontAccess) this$0).callGetFontSet(style.getFont());
		GlyphInfo glyphInfo = fontSet.getGlyphInfo(codepoint, ((FontAccess) this$0).getFilterFishyGlyphs());
		BakedGlyph bakedGlyph = style.isObfuscated() && codepoint != 32 ? fontSet.getRandomGlyph(glyphInfo) : fontSet.getGlyph(codepoint);
		boolean bold = style.isBold();
		float a = this.a;
		TextColor textColor = style.getColor();
		if (textColor != null) {
			int k = textColor.getValue();
			r = (float) (k >> 16 & 0xFF) / 255.0f * this.dimFactor;
			g = (float) (k >> 8 & 0xFF) / 255.0f * this.dimFactor;
			b = (float) (k & 0xFF) / 255.0f * this.dimFactor;
		} else {
			r = this.r;
			g = this.g;
			b = this.b;
		}
		float shadowOffset = this.dropShadow ? glyphInfo.getShadowOffset() : 0.0f;
		if (!(bakedGlyph instanceof EmptyGlyph)) {
			float m = bold ? glyphInfo.getBoldOffset() : 0.0f;
			TypewriterTrack typewriterTrack = taStyle.textanimator$getTypewriterTrack();
			int typingIndex = taStyle.textanimator$getTypewriterIndex();
			EffectSettings settings = new EffectSettings(
					codepoint,
					index + Math.max(typingIndex, 0),
					dropShadow,
					typewriterTrack,
					typingIndex);
			settings.x = this.x + shadowOffset;
			settings.y = this.y + shadowOffset;
			settings.r = r;
			settings.g = g;
			settings.b = b;
			settings.a = a;
			TextAnimationMode animationMode = TextAnimatorClient.getTextAnimationMode();
			for (int i = taStyle.textanimator$getEffects().size() - 1; i >= 0; i--) {
				Effect effect = taStyle.textanimator$getEffects().get(i);
				if (animationMode.shouldApply(effect)) {
					effect.apply(settings);
				}
			}
			r = settings.r;
			g = settings.g;
			b = settings.b;
			a = settings.a;
			if (a != 0) {
				VertexConsumer vertexConsumer = this.bufferSource.getBuffer(bakedGlyph.renderType(this.mode));
				((FontAccess) this$0).callRenderChar(
						bakedGlyph,
						bold,
						style.isItalic(),
						m,
						settings.x,
						settings.y,
						this.pose,
						vertexConsumer,
						r,
						g,
						b,
						a,
						this.packedLightCoords);
				for (Effect effect : taStyle.textanimator$getEffects()) {
					if (!animationMode.shouldApply(effect))
						continue;

					if (effect instanceof BlurEffect) {
						renderBlurEffect(
								(BlurEffect) effect,
								bakedGlyph,
								bold,
								style.isItalic(),
								m,
								settings,
								this.pose,
								vertexConsumer,
								r,
								g,
								b,
								a);
					}
				}
			}
		}
		float m = glyphInfo.getAdvance(bold);
		if (a != 0 && style.isStrikethrough()) {
			this.addEffect(new BakedGlyph.Effect(
					this.x + shadowOffset - 1.0f,
					this.y + shadowOffset + 4.5f,
					this.x + shadowOffset + m,
					this.y + shadowOffset + 4.5f - 1.0f,
					0.01f,
					r,
					g,
					b,
					a));
		}
		if (a != 0 && style.isUnderlined()) {
			this.addEffect(new BakedGlyph.Effect(
					this.x + shadowOffset - 1.0f,
					this.y + shadowOffset + 9.0f,
					this.x + shadowOffset + m,
					this.y + shadowOffset + 9.0f - 1.0f,
					0.01f,
					r,
					g,
					b,
					a));
		}
		this.x += m;
		cir.setReturnValue(true);
	}
	private void renderBlurEffect(
			BlurEffect blurEffect, BakedGlyph bakedGlyph, boolean bold, boolean italic,
			float boldOffset, EffectSettings settings, Matrix4f pose,
			VertexConsumer vertexConsumer, float r, float g, float b, float a) {
		int passes = Math.max(4, blurEffect.passes);
		float radius = blurEffect.radius;
		float alphaMul = blurEffect.alphaMul;

		renderRadialPasses(
				bakedGlyph, bold, italic, boldOffset, settings, pose, vertexConsumer,
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
			if (alpha <= 0) {
				continue;
			}

			float[] color = colorTransformer.apply(new float[]{baseR, baseG, baseB, alpha});

			((FontAccess) this$0).callRenderChar(
					bakedGlyph, bold, italic, boldOffset,
					settings.x + dx, settings.y + dy, pose, vertexConsumer,
					color[0], color[1], color[2], color[3], packedLightCoords);
		}
	}
	@Shadow
	protected abstract void addEffect(BakedGlyph.Effect effect);

}