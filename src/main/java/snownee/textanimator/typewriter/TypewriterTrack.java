package snownee.textanimator.typewriter;

import net.minecraft.Util;

public class TypewriterTrack {
	public long changedSince = Util.getMillis();
	public int index;

	public void update() {
		long now = Util.getMillis();
		if (now - changedSince > 40) {
			changedSince = changedSince + 40;
			index++;
		}
	}
}
