package snownee.textanimator.effect;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import snownee.textanimator.effect.params.Params;

public class BounceEffect extends BaseEffect {
	private final float amp;
	private final float speed;
	private final float phase;

	public BounceEffect(Params params) {
		super(params);
		this.amp = (float) params.getDouble("a").orElse(1.0);
		this.speed = (float) params.getDouble("f").orElse(1.0);
		this.phase = (float) params.getDouble("w").orElse(1.0);
	}

	@Override
	public void apply(EffectSettings settings) {
		float t = (Util.getMillis() * 0.001f * speed - settings.index * phase * 0.2f) % 1;
		float offset = 0;
		if (t < 0.2f) {
			offset = Mth.sin(t / 0.2f * Mth.HALF_PI);
		} else if (t < 0.8f) {
			t = (t - 0.2f) / 0.6f;
			/*
			The MIT License

			Copyright (c) 2010-2012 Tween.js authors.

			Easing equations Copyright (c) 2001 Robert Penner http://robertpenner.com/easing/

			Permission is hereby granted, free of charge, to any person obtaining a copy
			of this software and associated documentation files (the "Software"), to deal
			in the Software without restriction, including without limitation the rights
			to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
			copies of the Software, and to permit persons to whom the Software is
			furnished to do so, subject to the following conditions:

			The above copyright notice and this permission notice shall be included in
			all copies or substantial portions of the Software.

			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
			IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
			FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
			AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
			LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
					OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
			THE SOFTWARE.
			*/
			if (t < 1 / 2.75) {
				offset = 7.5625f * t * t;
			} else if (t < 2 / 2.75) {
				offset = 7.5625f * (t -= 1.5f / 2.75f) * t + 0.75f;
			} else if (t < 2.5 / 2.75) {
				offset = 7.5625f * (t -= 2.25f / 2.75f) * t + 0.9375f;
			} else {
				offset = 7.5625f * (t -= 2.625f / 2.75f) * t + 0.984375f;
			}
			offset = 1 - offset;
		}
		settings.y -= offset * amp * 4f;
	}

	@Override
	public String getName() {
		return "bounce";
	}
}