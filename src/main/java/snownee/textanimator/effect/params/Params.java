package snownee.textanimator.effect.params;

import java.util.Optional;
import java.util.OptionalDouble;

import org.jetbrains.annotations.Nullable;

public interface Params {
	@Nullable Boolean getBool(String key);

	OptionalDouble getDouble(String key);

	Optional<String> getString(String key);

	Optional<Object> getRaw(String key);
}
