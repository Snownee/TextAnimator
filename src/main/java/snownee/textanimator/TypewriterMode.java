package snownee.textanimator;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.OptionEnum;

public enum TypewriterMode implements OptionEnum {
	BY_CHAR("byChar"), BY_WORD("byWord");

	private static final TypewriterMode[] BY_ID = values();
	private final String key;

	TypewriterMode(String key) {
		this.key = "options.textanimator.typewriterMode." + key;
	}

	public static TypewriterMode byId(int i) {
		return BY_ID[i];
	}

	@Override
	public int getId() {
		return ordinal();
	}

	@Override
	public @NotNull String getKey() {
		return key;
	}
}
