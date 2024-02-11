package snownee.textanimator.typewriter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class TypewriterTracks {
	private static final TypewriterTracks INSTANCE = new TypewriterTracks();
	private final Cache<Object, TypewriterTrack> cache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.SECONDS).build();

	public static TypewriterTracks getInstance() {
		return INSTANCE;
	}

	public TypewriterTrack get(Object key) {
		try {
			return cache.get(key, () -> new TypewriterTrack());
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
