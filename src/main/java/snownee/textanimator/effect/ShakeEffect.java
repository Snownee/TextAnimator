package snownee.textanimator.effect;

import com.google.gson.JsonObject;

import net.minecraft.Util;
import net.minecraft.world.phys.Vec2;
import snownee.textanimator.TextAnimator;

public class ShakeEffect implements Effect {
	public ShakeEffect(JsonObject params) {
	}

	@Override
	public void apply(EffectSettings settings) {
		Vec2 dir = TextAnimator.RANDOM_DIR[(int) (Util.getMillis() * 0.01F + settings.codepoint + settings.index) % TextAnimator.RANDOM_DIR.length];
		settings.x += dir.x * 0.6F;
		settings.y += dir.y * 0.6F;
	}

	@Override
	public String getName() {
		return "shake";
	}
}
