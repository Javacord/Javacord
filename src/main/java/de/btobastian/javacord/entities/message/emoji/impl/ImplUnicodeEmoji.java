package de.btobastian.javacord.entities.message.emoji.impl;

import de.btobastian.javacord.entities.message.emoji.CustomEmoji;
import de.btobastian.javacord.entities.message.emoji.Emoji;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The representation of a unicode {@link Emoji}.
 */
public class ImplUnicodeEmoji implements Emoji {

    /**
     * A static map which contains all unicode emojis.
     */
    // Can be static, because unicode emojis are really unique, even for different discord accounts/shards.
    private static final ConcurrentHashMap<String, ImplUnicodeEmoji> unicodeEmojis = new ConcurrentHashMap<>();

    /**
     * The emoji string.
     */
    private final String emoji;

    /**
     * Creates a new unicode emoji.
     *
     * @param emoji The emoji string.
     */
    private ImplUnicodeEmoji(String emoji) {
        this.emoji = emoji;
    }

    /**
     * Gets a unicode emoji by it's string representation.
     *
     * @param emoji The emoji string.
     * @return The object, representing the emoji from the given string.
     */
    public static ImplUnicodeEmoji fromString(String emoji) {
        return unicodeEmojis.computeIfAbsent(emoji, key -> new ImplUnicodeEmoji(emoji));
    }

    @Override
    public String getMentionTag() {
        return emoji;
    }

    @Override
    public Optional<String> asUnicodeEmoji() {
        return Optional.of(emoji);
    }

    @Override
    public Optional<CustomEmoji> asCustomEmoji() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return String.format("UnicodeEmoji (emoji: %s)", emoji);
    }
}
