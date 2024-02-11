package snownee.textanimator.duck;

import net.minecraft.client.OptionInstance;
import snownee.textanimator.TextAnimationStatus;

public interface TAOptions {
	OptionInstance<TextAnimationStatus> textanimator$getTextAnimation();

	OptionInstance<Integer> textanimator$getTypewriterSpeed();
}
