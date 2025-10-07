package snownee.textanimator.effect.params;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;

public record TypedParams(ImmutableMap<String, Object> map) implements Params {
	@Override
	public @Nullable Boolean getBool(String key) {
		return map.get(key) instanceof Boolean bl ? bl : null;
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

	@Override
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(' ').append(entry.getKey()).append('=').append(entry.getValue());
		}
		return sb.toString();
	}
}
