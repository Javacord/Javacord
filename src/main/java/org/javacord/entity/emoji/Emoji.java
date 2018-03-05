package org.javacord.entity.emoji;

import org.javacord.entity.Mentionable;

import java.util.Optional;

/**
 * This class represents an emoji which can be a custom emoji (known or unknown) or a unicode emoji.
 */
public interface Emoji extends Mentionable {

    /**
     * Gets the emoji as unicode emoji.
     *
     * @return The emoji as unicode emoji.
     */
    Optional<String> asUnicodeEmoji();

    /**
     * Gets the emoji as custom emoji.
     *
     * @return The emoji as custom emoji.
     */
    Optional<CustomEmoji> asCustomEmoji();

    /**
     * Gets the emoji as known custom emoji.
     *
     * @return The emoji as known custom emoji.
     */
    Optional<KnownCustomEmoji> asKnownCustomEmoji();

    /**
     * Checks if the emoji is animated.
     * Always returns <code>false</code> for unicode emojis.
     *
     * @return Whether the emoji is animated or not.
     */
    boolean isAnimated();

    /**
     * Checks if the emoji is a unicode.
     *
     * @return Whether the emoji is a unicode emoji or not.
     */
    default boolean isUnicodeEmoji() {
        return asUnicodeEmoji().isPresent();
    }

    /**
     * Checks if the emoji is a custom emoji.
     *
     * @return Whether the emoji is a custom emoji or not.
     */
    default boolean isCustomEmoji() {
        return asCustomEmoji().isPresent();
    }

    /**
     * Checks if the emoji is a known custom emoji.
     *
     * @return Whether the emoji is a known custom emoji or not.
     */
    default boolean isKnownCustomEmoji() {
        return asKnownCustomEmoji().isPresent();
    }

}
