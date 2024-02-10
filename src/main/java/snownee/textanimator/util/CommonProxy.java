package snownee.textanimator.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import snownee.textanimator.duck.TALineBreakFinder;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;

public class CommonProxy {
	public static final Logger LOGGER = LoggerFactory.getLogger("TextAnimator");

	public static Style clone(Style style) {
		return style.withClickEvent(style.getClickEvent());
	}

	public static boolean textanimator$iterateFormatted(String string, int i, Style style, Style plainStyle, FormattedCharSink formattedCharSink) {
		int j = string.length();
		Style curStyle = style;
		int realIndex = -1;
		int skipped = 0;
		TAStyle taStyle = (TAStyle) style;
		if (taStyle.textanimator$getTypewriterTrack() != null) {
			realIndex = Math.max(taStyle.textanimator$getTypewriterIndex() + i, 0);
			System.out.println(string + " " + realIndex);
		}
		main:
		for (int k = i; k < j; ++k) {
			if (realIndex != -1) {
				++realIndex;
			}
			char d;
			char c = string.charAt(k);
			if (c == '§') {
				if (k + 1 >= j) break;
				d = string.charAt(k + 1);
				ChatFormatting chatFormatting = ChatFormatting.getByCode(d);
				if (chatFormatting != null) {
					curStyle = chatFormatting == ChatFormatting.RESET ? plainStyle : curStyle.applyLegacyFormat(chatFormatting);
					if (chatFormatting == ChatFormatting.RESET && realIndex != -1) {
						curStyle = CommonProxy.clone(curStyle);
					}
					skipped += 2;
					setIndex(curStyle, realIndex, skipped, formattedCharSink);
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
							skipped += l - k + 1;
							setIndex(curStyle, realIndex, skipped, formattedCharSink);
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
				d = string.charAt(k + 1);
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
			if (feedChar(curStyle, formattedCharSink, k, c)) continue;
			return false;
		}
		return true;
	}

	private static boolean feedChar(Style style, FormattedCharSink formattedCharSink, int i, char c) {
		return Character.isSurrogate(c) ? formattedCharSink.accept(i, style, 65533) : formattedCharSink.accept(i, style, c);
	}

	private static void setIndex(Style style, int realIndex, int skipped, FormattedCharSink formattedCharSink) {
		if (realIndex != -1) {
			((TAStyle) style).textanimator$setTypewriterIndex(realIndex);
			if (formattedCharSink instanceof TALineBreakFinder finder) {
				finder.textanimator$setSkippedChars(skipped);
			}
		}
	}

}
