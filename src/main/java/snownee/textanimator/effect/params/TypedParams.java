package snownee.textanimator.effect.params;

import java.util.Optional;
import java.util.OptionalDouble;

import com.machinezoo.noexception.optional.OptionalBoolean;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;

public record TypedParams(ImmutableMap<String, Object> map) implements Params {
	@Override
	public @Nullable OptionalBoolean getBool(String key) {
		Object value = map.get(key);
		if (value instanceof Boolean bool) {
			return OptionalBoolean.of(bool);
		}
		if (value instanceof OptionalBoolean optBool) {
			return optBool;
		}
		return null;
	}

	@Override
	public OptionalDouble getDouble(String key) {
		return map.get(key) instanceof Number number ? OptionalDouble.of(number.doubleValue()) : OptionalDouble.empty();
	}

	@Override
	public Optional<String> getString(String key) {
		return map.get(key) instanceof String string ? Optional.of(string) : Optional.empty();
	}

	@Override
	public Optional<Object> getRaw(String key) {
		return Optional.ofNullable(map.get(key));
	}
}
