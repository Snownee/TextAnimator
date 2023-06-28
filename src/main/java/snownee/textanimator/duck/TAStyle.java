package snownee.textanimator.duck;

import java.util.List;

import com.google.common.collect.ImmutableList;

import snownee.textanimator.effect.Effect;

public interface TAStyle {
	ImmutableList<Effect> textanimator$getEffects();

	void textanimator$setEffects(ImmutableList<Effect> effects);
}
