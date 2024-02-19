package snownee.textanimator.util;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import net.minecraft.util.StringDecomposer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.TypewriterMode;
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
		int lastBoundary = i;
		int charsEaten = i;
		IntList boundaries = IntList.of();
		TAStyle taStyle = (TAStyle) style;
		boolean byWord = TextAnimatorClient.getTypewriterMode() == TypewriterMode.BY_WORD;
		if (taStyle.textanimator$getTypewriterTrack() != null) {
			typingIndex = taStyle.textanimator$getTypewriterIndex();
			if (typingIndex == -1 && string.length() > 1) {
				Locale locale = CommonProxy.getLocale();
				BreakIterator breakIterator = byWord ? BreakIterator.getLineInstance(locale) : BreakIterator.getCharacterInstance(locale);
				StringBuilder sb = new StringBuilder();
				StringDecomposer.iterateFormatted(string, i, Style.EMPTY, (index, style1, codePoint) -> {
					sb.appendCodePoint(codePoint);
					return true;
				});
				breakIterator.setText(sb.toString());
//				ArrayList<String> words = new ArrayList<>();
				boundaries = new IntArrayList();
				int start = breakIterator.first();
				for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
//					words.add(sb.substring(start, end));
					boundaries.add(i + start);
				}
//				System.out.println(words);
				typingIndex = i;
			}
		}
		main:
		for (int k = i; k < j; ++k) {
			char c = string.charAt(k);
			if (!boundaries.isEmpty() && charsEaten >= boundaries.getInt(0)) {
				typingIndex += byWord ? Mth.clamp(charsEaten - lastBoundary, 1, 5) : 1;
				lastBoundary = boundaries.removeInt(0);
				curStyle = CommonProxy.clone(curStyle);
				((TAStyle) curStyle).textanimator$setTypewriterIndex(typingIndex);
			}
			if (c == '§') {
				if (k + 1 >= j) {
					break;
				}
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
						if (split.length == 0 || split[0].isEmpty()) {
							break;
						}
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
			++charsEaten;
			if (Character.isHighSurrogate(c)) {
				if (k + 1 >= j) {
					if (formattedCharSink.accept(k, curStyle, 65533)) {
						break;
					}
					return false;
				}
				char d = string.charAt(k + 1);
				if (Character.isLowSurrogate(d)) {
					if (!formattedCharSink.accept(k, curStyle, Character.toCodePoint(c, d))) {
						return false;
					}
					++k;
					++charsEaten;
					continue;
				}
				if (formattedCharSink.accept(k, curStyle, 65533)) {
					continue;
				}
				return false;
			}
			if (StringDecomposer.feedChar(curStyle, formattedCharSink, k, c)) {
				continue;
			}
			return false;
		}
		return true;
	}

	public static Locale getLocale() {
		return Locale.getDefault();
	}

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		if (FMLEnvironment.dist.isClient()) {
			ClientProxy.onEffectTypeRegistered(type, factory);
		}
	}
}
