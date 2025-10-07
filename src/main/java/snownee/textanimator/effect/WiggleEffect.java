package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.effect.params.Params;

public class WiggleEffect extends BaseEffect {
	private final float amp, speed, phase;

	public WiggleEffect(Params params) {
		super(params);
		this.amp = (float) params.getDouble("a").orElse(1.0);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.phase = (float) params.getDouble("w").orElse(1.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		Vec2 dir = TextAnimatorClient.getRandomDirection(settings.codepoint);
		float delta = Mth.sin(Util.getMillis() * 0.01F * speed + settings.index * 2F * phase) * 1.5F * amp;
		settings.x += dir.x * delta;
		settings.y += dir.y * delta;
	}

	@Override
	public String getName() {
		return "wiggle";
	}
}
