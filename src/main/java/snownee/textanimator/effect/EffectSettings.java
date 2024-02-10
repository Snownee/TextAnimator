package snownee.textanimator.effect;

import org.jetbrains.annotations.Nullable;

import snownee.textanimator.typewriter.TypewriterTrack;

public class EffectSettings {
	public final int codepoint;
	public final int index;
	public final boolean isShadow;
	@Nullable
	public final TypewriterTrack typewriterTrack;
	public float x;
	public float y;
	public float r;
	public float g;
	public float b;
	public float a;

	public EffectSettings(int codepoint, int index, boolean isShadow, @Nullable TypewriterTrack typewriterTrack) {
		this.codepoint = codepoint;
		this.index = index;
		this.isShadow = isShadow;
		this.typewriterTrack = typewriterTrack;
		if (typewriterTrack != null) {
			typewriterTrack.update();
		}
	}
}
