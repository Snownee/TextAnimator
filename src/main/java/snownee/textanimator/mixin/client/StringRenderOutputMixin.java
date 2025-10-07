package snownee.textanimator.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
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
import snownee.textanimator.duck.TABakedGlyph;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.EffectSettings;
import snownee.textanimator.effect.NeonEffect;
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
			settings.shadowOffset = shadowOffset;
			settings.siblings = Lists.newArrayList(settings);
			TextAnimationMode animationMode = TextAnimatorClient.getTextAnimationMode();
			for (int i = taStyle.textanimator$getEffects().size() - 1; i >= 0; i--) {
				Effect effect = taStyle.textanimator$getEffects().get(i);
				if (animationMode.shouldApply(effect)) {
					int size = settings.siblings.size();
					for (int j = 0; j < size; j++) {
						effect.apply(settings.siblings.get(j));
					}
				}
			}
			for (EffectSettings sibling : settings.siblings) {
				textanimator$renderChar(sibling, codepoint, style, fontSet, glyphInfo, bakedGlyph);
			}
			r = settings.r;
			g = settings.g;
			b = settings.b;
			a = settings.a;
		}
		float glyphWidth = glyphInfo.getAdvance(style.isBold());
		if (a != 0 && style.isStrikethrough()) {
			this.addEffect(new BakedGlyph.Effect(
					this.x + shadowOffset - 1.0f,
					this.y + shadowOffset + 4.5f,
					this.x + shadowOffset + glyphWidth,
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
					this.x + shadowOffset + glyphWidth,
					this.y + shadowOffset + 9.0f - 1.0f,
					0.01f,
					r,
					g,
					b,
					a));
		}
		this.x += glyphWidth;
		cir.setReturnValue(true);
	}

	@Unique
	private void textanimator$renderChar(
			EffectSettings settings,
			int oCodepoint,
			Style style,
			FontSet fontSet,
			GlyphInfo glyphInfo,
			BakedGlyph bakedGlyph) {
		if (settings.a == 0) {
			return;
		}
		if (settings.codepoint != oCodepoint) {
			bakedGlyph = fontSet.getGlyph(settings.codepoint);
			if (bakedGlyph instanceof EmptyGlyph) {
				return;
			}
		}

		VertexConsumer vertexConsumer = this.bufferSource.getBuffer(bakedGlyph.renderType(this.mode));
		Matrix4f pose = this.pose;
		if (settings.rot != 0) {
			float glyphWidth = glyphInfo.getAdvance(style.isBold());
			pose = TextAnimatorClient.rotate(pose, settings, settings.rot, glyphWidth / 2f, this$0.lineHeight / 2f);
		} else if (settings.pendRad != 0) {
			float glyphWidth = glyphInfo.getAdvance(style.isBold());
			pose = TextAnimatorClient.rotate(pose, settings, settings.pendRad, glyphWidth / 2f, 0);
		}

		TABakedGlyph glyph = (TABakedGlyph) bakedGlyph;
		glyph.textanimator$render(settings, style.isItalic(), 0f, pose, vertexConsumer, packedLightCoords);
		if (style.isBold()) {
			glyph.textanimator$render(settings, style.isItalic(), glyphInfo.getBoldOffset(), pose, vertexConsumer, packedLightCoords);
		}

		//TODO move to siblings
		float boldOffset = style.isBold() ? glyphInfo.getBoldOffset() : 0.0f;
		TAStyle taStyle = (TAStyle) style;
		TextAnimationMode animationMode = TextAnimatorClient.getTextAnimationMode();
		for (Effect effect : taStyle.textanimator$getEffects()) {
			if (effect instanceof NeonEffect neonEffect && animationMode.shouldApply(effect)) {
				TextAnimatorClient.renderNeonEffect(
						neonEffect,
						bakedGlyph,
						style.isBold(),
						style.isItalic(),
						boldOffset,
						settings,
						pose,
						vertexConsumer,
						settings.r,
						settings.g,
						settings.b,
						settings.a,
						packedLightCoords);
			}
		}
	}

	@Shadow
	protected abstract void addEffect(BakedGlyph.Effect effect);
}
