package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class RainbowEffect extends BaseEffect {
	private final float speed, phase;

	public RainbowEffect(Params params) {
		super(params);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.phase = (float) params.getDouble("w").orElse(1.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		if (settings.isShadow) {
			return;
		}
		int color = Mth.hsvToRgb(((Util.getMillis() * 0.02F * speed + settings.index * phase) % 30) / 30, 0.8F, 0.8F);
		settings.r = (color >> 16 & 255) / 255F;
		settings.g = (color >> 8 & 255) / 255F;
		settings.b = (color & 255) / 255F;
	}

	@Override
	public String getName() {
		return "rainb";
	}
}
