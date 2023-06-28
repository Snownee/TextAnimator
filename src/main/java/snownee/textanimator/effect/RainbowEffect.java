package snownee.textanimator.effect;

import com.google.gson.JsonObject;

import net.minecraft.Util;
import net.minecraft.util.Mth;

public class RainbowEffect implements Effect {
	public RainbowEffect(JsonObject params) {
	}

	@Override
	public void apply(EffectSettings settings) {
		int color = Mth.hsvToRgb(((Util.getMillis() * 0.02F + settings.index) % 30) / 30, 0.8F, 0.8F);
		settings.r = (color >> 16 & 255) / 255F;
		settings.g = (color >> 8 & 255) / 255F;
		settings.b = (color & 255) / 255F;
	}

	@Override
	public String getName() {
		return "rainb";
	}
}
