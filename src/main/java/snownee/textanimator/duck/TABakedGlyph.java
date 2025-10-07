package snownee.textanimator.duck;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;

import snownee.textanimator.effect.EffectSettings;

public interface TABakedGlyph {
	void textanimator$render(
			EffectSettings settings,
			boolean italic,
			float xOffset,
			Matrix4f pose,
			VertexConsumer vertexConsumer,
			int packedLightCoords);
}
