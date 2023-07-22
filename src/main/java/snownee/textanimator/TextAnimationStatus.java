package snownee.textanimator;

import java.util.Arrays;
import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;
import snownee.textanimator.effect.Effect;

public enum TextAnimationStatus implements OptionEnum {
	ALL("all"), NONE("none"), NO_RAINBOW("no_rainbow");

	private static final TextAnimationStatus[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(TextAnimationStatus::getId)).toArray(TextAnimationStatus[]::new);
	private final String key;

	TextAnimationStatus(String key) {
		this.key = "options.textanimator." + key;
	}

	public static TextAnimationStatus byId(int i) {
		return BY_ID[Mth.positiveModulo(i, BY_ID.length)];
	}

	@Override
	public int getId() {
		return ordinal();
	}

	@Override
	public @NotNull String getKey() {
		return key;
	}

	public boolean shouldApply(Effect effect) {
		return switch (this) {
			case ALL -> true;
			case NONE -> false;
			case NO_RAINBOW -> !effect.getName().equals("rainb");
		};
	}
}
