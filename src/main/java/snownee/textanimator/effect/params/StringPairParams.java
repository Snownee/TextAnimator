package snownee.textanimator.effect.params;

import com.machinezoo.noexception.optional.OptionalBoolean;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

public record StringPairParams(Map<String, String> params) implements Params {
	public StringPairParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public OptionalBoolean getBool(String key) {
		String s = params.get(key);
		if ("true".equals(s)) {
			return OptionalBoolean.of(true);
		}
		if ("false".equals(s)) {
			return OptionalBoolean.of(false);
		}
		return null;
	}

	@Override
	public OptionalDouble getDouble(String key) {
		String s = params.get(key);
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
		return Optional.ofNullable(params.get(key));
	}

	@Override
	public Optional<Object> getRaw(String key) {
		return Optional.ofNullable(params.get(key));
	}
}
