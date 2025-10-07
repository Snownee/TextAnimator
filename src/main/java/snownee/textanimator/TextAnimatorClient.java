package snownee.textanimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import snownee.textanimator.duck.TAOptions;
import snownee.textanimator.effect.EffectSettings;
import snownee.textanimator.effect.NeonEffect;
import snownee.textanimator.mixin.client.FontAccess;

public class TextAnimatorClient {
	private static Vec2[] RANDOM_DIR;
	private static int defaultTypewriterInterval;

	public static void init() {
		int len = 30;
		List<Vec2> dirs = new ArrayList<>(len);
		float step = (float) (Math.PI * 2 / len);
		for (int i = 0; i < len; i++) {
			float rad = step * i;
			dirs.add(new Vec2(Mth.cos(rad), Mth.sin(rad)));
		}
		Collections.shuffle(dirs);
		RANDOM_DIR = dirs.toArray(Vec2[]::new);
	}

	public static TextAnimationMode getTextAnimationMode() {
		//noinspection ConstantValue https://github.com/Snownee/TextAnimator/issues/25
		if (Minecraft.getInstance() == null || Minecraft.getInstance().options == null) {
			return TextAnimationMode.ALL;
		}
		var instance =
				((TAOptions) Minecraft.getInstance().options).textanimator$getTextAnimation();
		if (instance == null) {
			return TextAnimationMode.ALL;
		}
		return instance.get();
	}

	public static TypewriterMode getTypewriterMode() {
		//noinspection ConstantValue https://github.com/Snownee/TextAnimator/issues/25
		if (Minecraft.getInstance() == null || Minecraft.getInstance().options == null) {
			return TypewriterMode.BY_CHAR;
		}
		var instance = ((TAOptions) Minecraft.getInstance().options).textanimator$getTypewriterMode();
		if (instance == null) {
			return TypewriterMode.BY_CHAR;
		}
		return instance.get();
	}

	public static int defaultTypewriterInterval() {
		return defaultTypewriterInterval;
	}

	public static void setTypewriterSpeed(int speed) {
		speed = Mth.clamp(speed, 1, 9);
		int[] values = {4, 8, 12, 16, 20, 27, 36, 50, 70};
		defaultTypewriterInterval = values[9 - speed];
	}

	public static Vec2 getRandomDirection(int seed) {
		return RANDOM_DIR[Math.abs(seed) % RANDOM_DIR.length];
	}

	public static void renderNeonEffect(
			NeonEffect effect,
			BakedGlyph bakedGlyph,
			boolean bold, boolean italic, float boldOffset,
			EffectSettings settings,
			Matrix4f pose,
			VertexConsumer vertexConsumer,
			float baseR, float baseG, float baseB, float baseA,
			int packedLightCoords) {

		float alpha = baseA * effect.alphaMul;
		if (alpha <= 0) {
			return;
		}

		for (int i = 0; i < effect.passes; i++) {
			float angle = (float) (Math.PI * 2 * i / effect.passes);
			float dx = (float) Math.cos(angle) * effect.radius;
			float dy = (float) Math.sin(angle) * effect.radius;

			float x = settings.x + dx;
			float y = settings.y + dy;

			((FontAccess) Minecraft.getInstance().font).callRenderChar(
					bakedGlyph, bold, italic, boldOffset,
					x, y, pose, vertexConsumer,
					baseR, baseG, baseB, alpha, packedLightCoords
			);
		}
	}

	public static Matrix4f rotate(Matrix4f pose, EffectSettings settings, float rad, float oX, float oY) {
		pose = new Matrix4f(pose);
		pose.translate(settings.x + oX, settings.y + oY, 0);
		pose.rotate(Axis.ZP.rotation(rad));
		pose.translate(-oX, -oY, 0);
		settings.x = 0;
		settings.y = 0;
		return pose;
	}
}
