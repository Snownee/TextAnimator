package snownee.textanimator.effect;

public class EffectSettings {
	public final int codepoint;
	public final int index;
	public final boolean isShadow;
	public float x;
	public float y;
	public float r;
	public float g;
	public float b;
	public float a;

	public EffectSettings(int codepoint, int index, boolean isShadow) {
		this.codepoint = codepoint;
		this.index = index;
		this.isShadow = isShadow;
	}
}
