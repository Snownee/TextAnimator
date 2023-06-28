package snownee.textanimator.effect;

import com.google.gson.JsonObject;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import snownee.textanimator.TextAnimator;

public class WiggleEffect implements Effect {
	public WiggleEffect(JsonObject params) {
	}

	@Override
	public void apply(EffectSettings settings) {
		Vec2 dir = TextAnimator.RANDOM_DIR[settings.codepoint % TextAnimator.RANDOM_DIR.length];
		float delta = Mth.sin(Util.getMillis() * 0.01F + settings.index * 2F) * 1.5F;
		settings.x += dir.x * delta;
		settings.y += dir.y * delta;
	}

	@Override
	public String getName() {
		return "wiggle";
	}
}
