package snownee.textanimator.effect;

public class EffectSettings {
	public final int codepoint;
	public final int index;
	public float x;
	public float y;
	public float r;
	public float g;
	public float b;
	public float a;

	public EffectSettings(int codepoint, int index) {
		this.codepoint = codepoint;
		this.index = index;
	}
}
