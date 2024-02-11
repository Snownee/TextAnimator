package snownee.textanimator.duck;

import net.minecraft.client.OptionInstance;
import snownee.textanimator.TextAnimationMode;
import snownee.textanimator.TypewriterMode;

public interface TAOptions {
	OptionInstance<TextAnimationMode> textanimator$getTextAnimation();

	OptionInstance<Integer> textanimator$getTypewriterSpeed();

	OptionInstance<TypewriterMode> textanimator$getTypewriterMode();
}
