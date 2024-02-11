package snownee.textanimator.typewriter;

import net.minecraft.Util;
import snownee.textanimator.TextAnimatorClient;

public class TypewriterTrack {
	public long startedAt;
	public long changedSince;
	public int index;

	public TypewriterTrack() {
		startedAt = changedSince = Util.getMillis();
	}

	public void update() {
		long now = Util.getMillis();
		int interval = TextAnimatorClient.defaultTypewriterInterval();
		if (now - changedSince > interval) {
			changedSince = changedSince + interval;
			index++;
		}
	}
}
