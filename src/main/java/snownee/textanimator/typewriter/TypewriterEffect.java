package snownee.textanimator.typewriter;

import com.google.gson.JsonObject;

import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.EffectSettings;

public class TypewriterEffect implements Effect {

	public TypewriterEffect(JsonObject params) {
	}

	@Override
	public void apply(EffectSettings settings) {
		if (settings.typewriterTrack == null) {
			return;
		}
		if (settings.index >= settings.typewriterTrack.index) {
			settings.a = 0;
		}
	}

	@Override
	public String getName() {
		return "typewriter";
	}
}
