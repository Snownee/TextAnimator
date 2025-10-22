package snownee.textanimator;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public class TextAnimatorApi {
	private static final AtomicInteger PARSING_SUSPEND_COUNT = new AtomicInteger();

	public static AutoCloseable suspendParsing() {
		PARSING_SUSPEND_COUNT.incrementAndGet();
		return new AutoCloseable() {
			private boolean closed;

			@Override
			public void close() {
				if (!closed) {
					closed = true;
					resumeParsing();
				}
			}
		};
	}

	public static void resumeParsing() {
		PARSING_SUSPEND_COUNT.updateAndGet(value -> value > 0 ? value - 1 : 0);
	}

	public static boolean isParsingSuspended() {
		return PARSING_SUSPEND_COUNT.get() > 0;
	}

	public static void resetParsingState() {
		PARSING_SUSPEND_COUNT.set(0);
	}
}
