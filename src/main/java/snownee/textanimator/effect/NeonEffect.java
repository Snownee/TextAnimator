package snownee.textanimator.effect;

import snownee.textanimator.effect.params.Params;

public class NeonEffect implements Effect {
	public final int passes;
	public final float radius;
	public final float alphaMul;

	public NeonEffect(Params params) {
		this.passes = (int) Math.max(4, params.getDouble("passes").orElse(10));
		this.radius = (float) params.getDouble("radius").orElse(2);
		this.alphaMul = (float) params.getDouble("alpha").orElse(0.12);
	}

	@Override
	public void apply(EffectSettings settings) {
	}

	@Override
	public String getName() {
		return "neon";
	}

}