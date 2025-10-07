package snownee.textanimator.effect;

import net.minecraft.Util;
import snownee.textanimator.effect.params.Params;

public class PendulumEffect extends BaseEffect {
	private final float speed;
	private final float maxAngle;
	private final float radius;

	public PendulumEffect(Params params) {
		super(params);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.maxAngle = (float) params.getDouble("maxAngle").orElse(30.0);
		this.radius = (float) params.getDouble("radius").orElse(0.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		long t = Util.getMillis();
		double phase = (t * 0.002 * speed) - (settings.index * 0.1);

		float angleRad = (float) Math.toRadians(maxAngle);
		settings.pendRad = (float) (Math.sin(phase) * angleRad);

		if (radius != 0) {
			settings.x += (float) (Math.cos(phase) * radius);
			settings.y += (float) (Math.sin(phase) * radius);
		}
	}

	@Override
	public String getName() {
		return "pend";
	}
}