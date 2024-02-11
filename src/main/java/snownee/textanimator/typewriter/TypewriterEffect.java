package snownee.textanimator.typewriter;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.EffectSettings;
import snownee.textanimator.effect.params.Params;

public class TypewriterEffect implements Effect {

	public TypewriterEffect(Params params) {
	}

	@Nullable
	public static Pair<TypewriterEffect, Integer> find(String text) {
		if (!text.startsWith("<typewriter")) {
			return null;
		}
		int endIndex = text.indexOf('>');
		if (endIndex == -1) {
			return null;
		}
		Effect effect = Effect.create(text.substring(1, endIndex), true);
		if (effect instanceof TypewriterEffect) {
			return Pair.of((TypewriterEffect) effect, endIndex + 1);
		}
		return null;
	}

	@Override
	public void apply(EffectSettings settings) {
		if (settings.typewriterTrack == null) {
			return;
		}
		if (settings.typingIndex >= settings.typewriterTrack.index) {
			settings.a = 0;
		}
	}

	@Override
	public String getName() {
		return "typewriter";
	}
}
