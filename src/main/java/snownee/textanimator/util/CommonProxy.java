package snownee.textanimator.util;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;

public class CommonProxy {
	public static final Logger LOGGER = LoggerFactory.getLogger("TextAnimator");

	public static Style clone(Style style) {
		return style.withClickEvent(style.getClickEvent());
	}

	public static boolean iterateFormatted(String string, int i, Style style, Style plainStyle, FormattedCharSink formattedCharSink) {
		int j = string.length();
		Style curStyle = style;
		int typingIndex = -1;
		TAStyle taStyle = (TAStyle) style;
		if (taStyle.textanimator$getTypewriterTrack() != null) {
			typingIndex = Math.max(taStyle.textanimator$getTypewriterIndex() + i, 0);
			((TAStyle) style).textanimator$setTypewriterIndex(typingIndex);
		}
		main:
		for (int k = i; k < j; ++k) {
			char c = string.charAt(k);
			if (typingIndex != -1 && k != i) {
				++typingIndex;
				curStyle = CommonProxy.clone(curStyle);
				((TAStyle) curStyle).textanimator$setTypewriterIndex(typingIndex);
			}
			if (c == '§') {
				if (k + 1 >= j) break;
				char d = string.charAt(k + 1);
				ChatFormatting chatFormatting = ChatFormatting.getByCode(d);
				if (chatFormatting != null) {
					curStyle = chatFormatting == ChatFormatting.RESET ? plainStyle : curStyle.applyLegacyFormat(chatFormatting);
				}
				++k;
				continue;
			}
			if (c == '<') {
				StringBuilder sb = new StringBuilder();
				for (int l = k + 1; l < j; ++l) {
					char ch = string.charAt(l);
					if (ch == '>') {
						String[] split = StringUtils.split(sb.toString(), ' ');
						if (split.length == 0 || split[0].isEmpty()) break;
						ImmutableList<Effect> newEffects = null;
						ImmutableList<Effect> effects = ((TAStyle) curStyle).textanimator$getEffects();
						if (split[0].charAt(0) == '/') {
							String tagName = split[0].substring(1);
							if (!effects.isEmpty() && effects.get(effects.size() - 1).getName().equals(tagName)) {
								newEffects = effects.subList(0, effects.size() - 1);
							}
						} else {
							Effect effect = Effect.create(split, false);
							if (effect != null) {
								newEffects = ImmutableList.<Effect>builder().addAll(effects).add(effect).build();
							}
						}
						if (newEffects != null) {
							curStyle = CommonProxy.clone(curStyle);
							((TAStyle) curStyle).textanimator$setEffects(newEffects);
							k = l;
							continue main;
						} else {
							break;
						}
					}
					sb.append(ch);
				}
			}
			if (Character.isHighSurrogate(c)) {
				if (k + 1 >= j) {
					if (formattedCharSink.accept(k, curStyle, 65533)) break;
					return false;
				}
				char d = string.charAt(k + 1);
				if (Character.isLowSurrogate(d)) {
					if (!formattedCharSink.accept(k, curStyle, Character.toCodePoint(c, d))) {
						return false;
					}
					++k;
					continue;
				}
				if (formattedCharSink.accept(k, curStyle, 65533)) continue;
				return false;
			}
			if (StringDecomposer.feedChar(curStyle, formattedCharSink, k, c)) continue;
			return false;
		}
		return true;
	}

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientProxy.onEffectTypeRegistered(type, factory);
		}
	}
}
