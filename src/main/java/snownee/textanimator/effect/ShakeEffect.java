package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.world.phys.Vec2;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.effect.params.Params;

public class ShakeEffect extends BaseEffect {
	private final float amp, speed;

	public ShakeEffect(Params params) {
		super(params);
		this.amp = (float) params.getDouble("a").orElse(1.0);
		this.speed = (float) params.getDouble("f").orElse(1.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		Vec2 dir = TextAnimatorClient.getRandomDirection((int) (Util.getMillis() * 0.01F * speed + settings.codepoint + settings.index));
		settings.x += dir.x * 0.6F * amp;
		settings.y += dir.y * 0.6F * amp;
	}

	@Override
	public String getName() {
		return "shake";
	}
}
