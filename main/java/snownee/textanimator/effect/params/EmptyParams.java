package snownee.textanimator.effect.params;

import java.util.Optional;
import java.util.OptionalDouble;

import org.jetbrains.annotations.Nullable;

public final class EmptyParams implements Params {
	public static final EmptyParams INSTANCE = new EmptyParams();

	private EmptyParams() {
	}

	@Override
	public @Nullable Boolean getBool(String key) {
		return null;
	}

	@Override
	public OptionalDouble getDouble(String key) {
		return OptionalDouble.empty();
	}

	@Override
	public Optional<String> getString(String key) {
		return Optional.empty();
	}

	@Override
	public Optional<Object> getRaw(String key) {
		return Optional.empty();
	}
}
