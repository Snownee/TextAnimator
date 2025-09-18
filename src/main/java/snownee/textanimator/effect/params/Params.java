package snownee.textanimator.effect.params;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalDouble;

public interface Params {
	@Nullable Boolean getBool(String key);

	OptionalDouble getDouble(String key);

	Optional<String> getString(String key);

	Optional<Object> getRaw(String key);
}
