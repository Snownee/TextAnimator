package snownee.textanimator.api;

import snownee.textanimator.util.CommonProxy;

public class TextAnimatorApi {
	public static AutoCloseable suspendParsing() {
		return CommonProxy.suspendParsing();
	}

	public static void resumeParsing() {
		CommonProxy.resumeParsing();
	}

	public static boolean isParsingSuspended() {
		return CommonProxy.isParsingSuspended();
	}

	public static boolean setPlayerParsingSuspended(boolean suspended) {
		return CommonProxy.setPlayerParsingSuspended(suspended);
	}

	public static boolean isPlayerParsingSuspended() {
		return CommonProxy.isPlayerParsingSuspended();
	}
}
