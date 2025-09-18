package snownee.textanimator.duck;

import com.google.common.collect.ImmutableList;

import snownee.textanimator.effect.Effect;
import snownee.textanimator.typewriter.TypewriterTrack;

public interface TAStyle {
	ImmutableList<Effect> textanimator$getEffects();

	void textanimator$setEffects(ImmutableList<Effect> effects);

	void textanimator$addEffect(Effect effect);

	TypewriterTrack textanimator$getTypewriterTrack();

	void textanimator$setTypewriterTrack(TypewriterTrack track);

	int textanimator$getTypewriterIndex();

	void textanimator$setTypewriterIndex(int index);
}
