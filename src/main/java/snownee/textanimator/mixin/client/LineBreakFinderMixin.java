package snownee.textanimator.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.StringSplitter;
import snownee.textanimator.duck.TALineBreakFinder;

@Mixin(StringSplitter.LineBreakFinder.class)
public class LineBreakFinderMixin implements TALineBreakFinder {
	@Unique
	private int textanimator$skippedChars;

	@Override
	public int textanimator$getSkippedChars() {
		return textanimator$skippedChars;
	}

	@Override
	public void textanimator$setSkippedChars(int skippedChars) {
		textanimator$skippedChars = skippedChars;
	}
}
