package snownee.textanimator.effect;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import snownee.textanimator.typewriter.TypewriterTrack;

public class EffectSettings {
	public final int index;
	public final boolean isShadow;
	@Nullable
	public final TypewriterTrack typewriterTrack;
	public final int typingIndex;
	public int codepoint;
	public float x;
	public float y;
	public float r;
	public float g;
	public float b;
	public float a;
	public float pendRad;
	public float rot;
	public float maskTop;
	public float maskBottom;
	//	public float maskLeft;
	//	public float maskRight;
	public List<EffectSettings> siblings = List.of();
	public float shadowOffset;

	public EffectSettings(
			int codepoint,
			int index,
			boolean isShadow,
			@Nullable TypewriterTrack typewriterTrack,
			int typingIndex) {
		this.codepoint = codepoint;
		this.index = index;
		this.isShadow = isShadow;
		this.typewriterTrack = typewriterTrack;
		this.typingIndex = typingIndex;
		if (typewriterTrack != null) {
			typewriterTrack.update();
		}
	}

	public EffectSettings copy() {
		EffectSettings settings = new EffectSettings(codepoint, index, isShadow, typewriterTrack, typingIndex);
		settings.x = x;
		settings.y = y;
		settings.r = r;
		settings.g = g;
		settings.b = b;
		settings.a = a;
		settings.pendRad = pendRad;
		settings.rot = rot;
		settings.maskTop = maskTop;
		settings.maskBottom = maskBottom;
//		settings.maskLeft = maskLeft;
//		settings.maskRight = maskRight;
		settings.siblings = siblings;
		settings.shadowOffset = shadowOffset;
		return settings;
	}
}
