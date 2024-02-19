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
		TAStyle taStyle = (TAStyle) style;
		if (taStyle.textanimator$getEffects().isEmpty()) {
			return;
		}
		float b;
		float g;
		float r;
		FontSet fontSet = this$0.getFontSet(style.getFont());
		GlyphInfo glyphInfo = fontSet.getGlyphInfo(codepoint, this$0.filterFishyGlyphs);
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
			EffectSettings settings = new EffectSettings(codepoint, index + Math.max(typingIndex, 0), dropShadow, typewriterTrack, typingIndex);
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
			VertexConsumer vertexConsumer = this.bufferSource.getBuffer(bakedGlyph.renderType(this.mode));
			this$0.renderChar(bakedGlyph, bold, style.isItalic(), m, settings.x, settings.y, this.pose, vertexConsumer, settings.r, settings.g, settings.b, settings.a, this.packedLightCoords);
		}
		float m = glyphInfo.getAdvance(bold);
		if (style.isStrikethrough()) {
			this.addEffect(new BakedGlyph.Effect(this.x + shadowOffset - 1.0f, this.y + shadowOffset + 4.5f, this.x + shadowOffset + m, this.y + shadowOffset + 4.5f - 1.0f, 0.01f, r, g, b, a));
		}
		if (style.isUnderlined()) {
			this.addEffect(new BakedGlyph.Effect(this.x + shadowOffset - 1.0f, this.y + shadowOffset + 9.0f, this.x + shadowOffset + m, this.y + shadowOffset + 9.0f - 1.0f, 0.01f, r, g, b, a));
		}
		this.x += m;
		cir.setReturnValue(true);
	}

	@Shadow
	protected abstract void addEffect(BakedGlyph.Effect effect);

}
