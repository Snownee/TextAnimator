package snownee.textanimator.typewriter;

import net.minecraft.Util;
import snownee.textanimator.TextAnimatorClient;

public class TypewriterTrack {
	public long changedSince = Util.getMillis();
	public int index;

	public void update() {
		long now = Util.getMillis();
		int interval = TextAnimatorClient.defaultTypewriterInterval();
		if (now - changedSince > interval) {
			changedSince = changedSince + interval;
			index++;
		}
	}
}
