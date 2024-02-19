package snownee.textanimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import snownee.textanimator.duck.TAOptions;
import snownee.textanimator.effect.EffectFactory;
import snownee.textanimator.effect.RainbowEffect;
import snownee.textanimator.effect.ShakeEffect;
import snownee.textanimator.effect.WaveEffect;
import snownee.textanimator.effect.WiggleEffect;
import snownee.textanimator.typewriter.TypewriterEffect;

public class TextAnimatorClient {
	public static Vec2[] RANDOM_DIR;
	//	public static final Set<Class<?>> SCREENS_DISABLED = Sets.newIdentityHashSet();
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

//		registerDisabledScreen(BookEditScreen.class);

		EffectFactory.register("typewriter", TypewriterEffect::new);
		EffectFactory.register("shake", ShakeEffect::new);
		EffectFactory.register("wave", WaveEffect::new);
		EffectFactory.register("rainb", RainbowEffect::new);
		EffectFactory.register("wiggle", WiggleEffect::new);
	}

	public static TextAnimationMode getTextAnimationMode() {
		OptionInstance<TextAnimationMode> instance = ((TAOptions) Minecraft.getInstance().options).textanimator$getTextAnimation();
		if (instance == null) {
			return TextAnimationMode.ALL;
		}
		return instance.get();
	}

	public static TypewriterMode getTypewriterMode() {
		OptionInstance<TypewriterMode> instance = ((TAOptions) Minecraft.getInstance().options).textanimator$getTypewriterMode();
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

//	public static synchronized void registerDisabledScreen(Class<?> screen) {
//		SCREENS_DISABLED.add(screen);
//	}
}
