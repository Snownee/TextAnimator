package snownee.textanimator.effect.params;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import org.jetbrains.annotations.Nullable;

public record StringPairParams(Map<String, String> map) implements Params {
	public StringPairParams(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public @Nullable Boolean getBool(String key) {
		String s = map.get(key);
		if ("true".equals(s)) {
			return true;
		}
		if ("false".equals(s)) {
			return false;
		}
		return null;
	}

	@Override
	public OptionalDouble getDouble(String key) {
		String s = map.get(key);
		if (s == null) {
			return OptionalDouble.empty();
		}
		try {
			return OptionalDouble.of(Double.parseDouble(s));
		} catch (NumberFormatException e) {
			return OptionalDouble.empty();
		}
	}

	@Override
	public Optional<String> getString(String key) {
		return Optional.ofNullable(map.get(key));
	}

	@Override
	public Optional<Object> getRaw(String key) {
		return Optional.ofNullable(map.get(key));
	}

	@Override
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(' ').append(entry.getKey()).append('=').append(entry.getValue());
		}
		return sb.toString();
	}
}
