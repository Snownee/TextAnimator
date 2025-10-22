package snownee.textanimator.util;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import net.minecraft.util.StringDecomposer;
import snownee.textanimator.TextAnimator;
import snownee.textanimator.TextAnimatorApi;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.TypewriterMode;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.EffectFactory;
import snownee.textanimator.effect.params.Params;
import snownee.textanimator.mixin.StringDecomposerAccess;

public class CommonProxy implements ModInitializer {
	public static Style clone(Style style) {
		Style copy = new Style(
				style.getColor(),
				style.isBold(),
				style.isItalic(),
				style.isUnderlined(),
				style.isStrikethrough(),
				style.isObfuscated(),
				style.getClickEvent(),
				style.getHoverEvent(),
				style.getInsertion(),
				style.getFont());
		((TAStyle) copy).textanimator$setEffects(((TAStyle) style).textanimator$getEffects());
		((TAStyle) copy).textanimator$setTypewriterTrack(((TAStyle) style).textanimator$getTypewriterTrack());
		((TAStyle) copy).textanimator$setTypewriterIndex(((TAStyle) style).textanimator$getTypewriterIndex());
		return copy;
	}

	public static boolean iterateFormatted(
			String string,
			int i,
			Style style,
			Style plainStyle,
			FormattedCharSink formattedCharSink) {
		int j = string.length();
		Style curStyle = style;
		int typingIndex = -1;
		int lastBoundary = i;
		int charsEaten = i;
		IntList boundaries = IntList.of();
		TAStyle taStyle = (TAStyle) style;
		boolean byWord = isPhysicalClient() && TextAnimatorClient.getTypewriterMode() == TypewriterMode.BY_WORD;
		if (taStyle.textanimator$getTypewriterTrack() != null) {
			typingIndex = taStyle.textanimator$getTypewriterIndex();
			if (typingIndex == -1 && string.length() > 1) {
				Locale locale = CommonProxy.getLocale();
				BreakIterator breakIterator = byWord ? BreakIterator.getLineInstance(locale) : BreakIterator.getCharacterInstance(locale);
				StringBuilder sb = new StringBuilder();
				StringDecomposer.iterateFormatted(
						string, i, Style.EMPTY, (index, style1, codePoint) -> {
							sb.appendCodePoint(codePoint);
							return true;
						});
				breakIterator.setText(sb.toString());
				boundaries = new IntArrayList();
				int start = breakIterator.first();
				for (int end = breakIterator.next(); end != BreakIterator.DONE; start = end, end = breakIterator.next()) {
					boundaries.add(i + start);
				}
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
			if (c == '<' && !TextAnimatorApi.isParsingSuspended()) {
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
							try {
								Effect effect = Effect.create(split, false);
								newEffects = ImmutableList.<Effect>builder().addAll(effects).add(effect).build();
							} catch (Exception ignored) {
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
			if (StringDecomposerAccess.callFeedChar(curStyle, formattedCharSink, k, c)) {
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
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientProxy.onEffectTypeRegistered(type, factory);
		}
	}

	public static String stripEffectTags(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		int start = 0;
		StringBuilder builder = null;
		while (true) {
			int open = input.indexOf('<', start);
			if (open == -1) {
				break;
			}
			int close = input.indexOf('>', open + 1);
			if (close == -1) {
				break;
			}
			String content = input.substring(open + 1, close);
			boolean remove = false;
			if (!content.isEmpty()) {
				if (content.charAt(0) == '/') {
					String type = content.substring(1);
					remove = EffectFactory.listTypes().contains(type);
				} else {
					try {
						Effect.create(content, true);
						remove = true;
					} catch (IllegalArgumentException ignored) {
					}
				}
			}
			if (remove) {
				if (builder == null) {
					builder = new StringBuilder(input.length());
				}
				builder.append(input, start, open);
				start = close + 1;
				continue;
			}
			if (builder != null) {
				builder.append(input, start, close + 1);
			}
			start = close + 1;
		}
		if (builder == null) {
			return input;
		}
		builder.append(input, start, input.length());
		return builder.toString();
	}

	public static boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public void onInitialize() {
		TextAnimator.init();
	}
}
