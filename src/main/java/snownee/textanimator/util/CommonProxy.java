package snownee.textanimator.util;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import net.minecraft.util.StringDecomposer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snownee.textanimator.TextAnimator;
import snownee.textanimator.TextAnimatorClient;
import snownee.textanimator.TypewriterMode;
import snownee.textanimator.duck.TAStyle;
import snownee.textanimator.effect.Effect;
import snownee.textanimator.effect.params.Params;
import snownee.textanimator.mixin.StringDecomposerAccess;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.BreakIterator;
import java.util.Locale;
import java.util.function.Function;

public class CommonProxy implements ModInitializer {
        public static final Logger LOGGER = LoggerFactory.getLogger("TextAnimator");

        private static final Constructor<Style> textanimator$styleConstructor;
        private static final Field textanimator$colorField;
        private static final Field textanimator$boldField;
        private static final Field textanimator$italicField;
        private static final Field textanimator$underlinedField;
        private static final Field textanimator$strikethroughField;
        private static final Field textanimator$obfuscatedField;
        private static final Field textanimator$clickEventField;
        private static final Field textanimator$hoverEventField;
        private static final Field textanimator$insertionField;
        private static final Field textanimator$fontField;

        static {
                try {
                        textanimator$styleConstructor = Style.class.getDeclaredConstructor(
                                        TextColor.class,
                                        Boolean.class,
                                        Boolean.class,
                                        Boolean.class,
                                        Boolean.class,
                                        Boolean.class,
                                        ClickEvent.class,
                                        HoverEvent.class,
                                        String.class,
                                        ResourceLocation.class);
                        textanimator$styleConstructor.setAccessible(true);
                        textanimator$colorField = textanimator$accessStyleField("color");
                        textanimator$boldField = textanimator$accessStyleField("bold");
                        textanimator$italicField = textanimator$accessStyleField("italic");
                        textanimator$underlinedField = textanimator$accessStyleField("underlined");
                        textanimator$strikethroughField = textanimator$accessStyleField("strikethrough");
                        textanimator$obfuscatedField = textanimator$accessStyleField("obfuscated");
                        textanimator$clickEventField = textanimator$accessStyleField("clickEvent");
                        textanimator$hoverEventField = textanimator$accessStyleField("hoverEvent");
                        textanimator$insertionField = textanimator$accessStyleField("insertion");
                        textanimator$fontField = textanimator$accessStyleField("font");
                } catch (ReflectiveOperationException e) {
                        throw new IllegalStateException("Failed to prepare style cloning", e);
                }
        }

        private static Field textanimator$accessStyleField(String name) throws NoSuchFieldException {
                Field field = Style.class.getDeclaredField(name);
                field.setAccessible(true);
                return field;
        }

        public static Style clone(Style style) {
                Style copy = textanimator$copyVanillaStyle(style);
                if (style instanceof TAStyle original && copy instanceof TAStyle clone) {
                        clone.textanimator$setEffects(original.textanimator$getEffects());
                        clone.textanimator$setTypewriterTrack(original.textanimator$getTypewriterTrack());
                        clone.textanimator$setTypewriterIndex(original.textanimator$getTypewriterIndex());
                }
                return copy;
        }

        private static @NotNull Style textanimator$copyVanillaStyle(Style style) {
                try {
                        return textanimator$styleConstructor.newInstance(
                                        textanimator$getField(textanimator$colorField, style),
                                        textanimator$getField(textanimator$boldField, style),
                                        textanimator$getField(textanimator$italicField, style),
                                        textanimator$getField(textanimator$underlinedField, style),
                                        textanimator$getField(textanimator$strikethroughField, style),
                                        textanimator$getField(textanimator$obfuscatedField, style),
                                        textanimator$getField(textanimator$clickEventField, style),
                                        textanimator$getField(textanimator$hoverEventField, style),
                                        textanimator$getField(textanimator$insertionField, style),
                                        textanimator$getField(textanimator$fontField, style));
                } catch (ReflectiveOperationException e) {
                        throw new IllegalStateException("Failed to clone style", e);
                }
        }

        @Contract(pure = true)
        @SuppressWarnings("unchecked")
        private static <T> T textanimator$getField(@NotNull Field field, Style style) throws IllegalAccessException {
                return (T) field.get(style);
        }

	public static boolean iterateFormatted(@NotNull String string, int i, Style style, Style plainStyle, FormattedCharSink formattedCharSink) {
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
				StringDecomposer.iterateFormatted(string, i, Style.EMPTY, (index, style1, codePoint) -> {
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
			if (c == '<') {
				StringBuilder sb = new StringBuilder();
				for (int l = k + 1; l < j; ++l) {
					char ch = string.charAt(l);
					if (ch == '>') {
                                                String[] split = StringUtils.split(sb.toString());
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
                                                        String tagName = split[0];
                                                        if (split.length == 1 && !effects.isEmpty() && effects.get(effects.size() - 1).getName().equals(tagName)) {
                                                                newEffects = effects.subList(0, effects.size() - 1);
                                                        } else {
                                                                try {
                                                                        Effect effect = Effect.create(split, false);
                                                                        if (!textanimator$hasClosingTag(string, l + 1, effect.getName())) {
                                                                                break;
                                                                        }
                                                                        newEffects = ImmutableList.<Effect>builder().addAll(effects).add(effect).build();
                                                                } catch (Exception ignored) {
                                                                }
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

        private static boolean textanimator$hasClosingTag(String text, int fromIndex, @NotNull String effectName) {
                if (effectName.isEmpty()) {
                        return false;
                }
                int index = fromIndex;
                while (index < text.length()) {
                        int open = text.indexOf('<', index);
                        if (open == -1) {
                                return false;
                        }
                        int close = text.indexOf('>', open + 1);
                        if (close == -1) {
                                return false;
                        }
                        if (close == open + 1) {
                                index = close + 1;
                                continue;
                        }
                        if (textanimator$isSlashClosingTag(text, open + 1, close, effectName)) {
                                return true;
                        }
                        if (textanimator$isMirrorClosingTag(text, open + 1, close, effectName)) {
                                return true;
                        }
                        index = close + 1;
                }
                return false;
        }

        private static boolean textanimator$isSlashClosingTag(String text, int start, int end, String effectName) {
                int nameStart = start;
                if (nameStart >= end || text.charAt(nameStart) != '/') {
                        return false;
                }
                ++nameStart;
                if (!text.regionMatches(nameStart, effectName, 0, effectName.length())) {
                        return false;
                }
                int index = nameStart + effectName.length();
                while (index < end && Character.isWhitespace(text.charAt(index))) {
                        ++index;
                }
                return index == end;
        }

        private static boolean textanimator$isMirrorClosingTag(@NotNull String text, int start, int end, String effectName) {
                if (!text.regionMatches(start, effectName, 0, effectName.length())) {
                        return false;
                }
                int index = start + effectName.length();
                while (index < end && Character.isWhitespace(text.charAt(index))) {
                        ++index;
                }
                return index == end;
        }

	public static void onEffectTypeRegistered(String type, Function<Params, Effect> factory) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientProxy.onEffectTypeRegistered(type, factory);
		}
	}

	@Override
	public void onInitialize() {
		TextAnimator.init();
	}

	public static boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
}
