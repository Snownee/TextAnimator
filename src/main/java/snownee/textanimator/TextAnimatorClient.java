package snownee.textanimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import snownee.textanimator.duck.TAOptions;

public class TextAnimatorClient {
	public static Vec2[] RANDOM_DIR;
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
		if (Minecraft.getInstance().options == null) {
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
		if (Minecraft.getInstance().options == null) {
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
}
