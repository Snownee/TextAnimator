package snownee.textanimator.util;

import net.fabricmc.api.ModInitializer;
import snownee.textanimator.TextAnimator;

//@Mod(TextAnimator.ID)
public class CommonProxy implements ModInitializer {

	@Override
	public void onInitialize() {
		TextAnimator.init();
	}
}
