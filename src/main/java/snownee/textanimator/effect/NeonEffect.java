package snownee.textanimator.effect;

import snownee.textanimator.effect.params.Params;

public class NeonEffect extends BaseEffect {
	public final int passes;
	public final float radius;
	public final float alphaMul;

	public NeonEffect(Params params) {
		super(params);
		this.passes = (int) Math.max(4, params.getDouble("p").orElse(10));
		this.radius = (float) params.getDouble("r").orElse(2);
		this.alphaMul = (float) params.getDouble("a").orElse(0.12);
	}

	@Override
	public void apply(EffectSettings settings) {
	}

	@Override
	public String getName() {
		return "neon";
	}

}