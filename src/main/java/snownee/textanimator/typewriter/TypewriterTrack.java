package snownee.textanimator.typewriter;

import net.minecraft.Util;
import snownee.textanimator.TextAnimatorClient;

public class TypewriterTrack {
	public long changedSince = Util.getMillis();
	public int index;

	public void update() {
		long now = Util.getMillis();
		if (now - changedSince > TextAnimatorClient.defaultTypewriterInterval()) {
			changedSince = changedSince + TextAnimatorClient.defaultTypewriterInterval();
			index++;
		}
	}
}
