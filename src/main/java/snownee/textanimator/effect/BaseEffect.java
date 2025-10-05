package snownee.textanimator.effect;

import snownee.textanimator.effect.params.Params;

public abstract class BaseEffect implements Effect {
	protected final Params params;

	public BaseEffect(Params params) {
		this.params = params;
	}

	@Override
	public String serialize() {
		return getName() + params.serialize();
	}
}
