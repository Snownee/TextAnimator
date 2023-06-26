package snownee.textanimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class TextAnimator {
	public static final Vec2[] RANDOM_DIR = new Vec2[30];

	public static void init() {
		List<Vec2> dirs = new ArrayList<>(RANDOM_DIR.length);
		float step = (float) (Math.PI * 2 / RANDOM_DIR.length);
		for (int i = 0; i < RANDOM_DIR.length; i++) {
			float rad = step * i;
			dirs.add(new Vec2(Mth.cos(rad), Mth.sin(rad)));
		}
		Collections.shuffle(dirs);
		dirs.toArray(RANDOM_DIR);
	}
}
