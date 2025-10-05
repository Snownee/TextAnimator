package snownee.textanimator.effect.params;

import java.util.Optional;
import java.util.OptionalDouble;

import com.machinezoo.noexception.optional.OptionalBoolean;

import org.jetbrains.annotations.Nullable;

public interface Params {
	@Nullable
	OptionalBoolean getBool(String key);

	OptionalDouble getDouble(String key);

	Optional<String> getString(String key);

	Optional<Object> getRaw(String key);
}
