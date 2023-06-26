package snownee.textanimator.effect;

import com.google.gson.JsonObject;

import net.minecraft.Util;
import net.minecraft.util.Mth;

public class WaveEffect implements Effect {
	public WaveEffect(JsonObject params) {
	}

	@Override
	public void apply(EffectSettings settings) {
		settings.y += Mth.sin(Util.getMillis() * 0.01F + settings.index) * 2;
	}

	@Override
	public String getName() {
		return "wave";
	}
}
