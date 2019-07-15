package org.javacord.api.entity.emoji;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.util.Specializable;

import java.util.Optional;

/**
 * This class represents an emoji which can be a custom emoji (known or unknown) or a unicode emoji.
 */
public interface Emoji extends Mentionable, Specializable<Emoji> {

    /**
     * Gets the emoji as a unicode emoji.
     *
     * @return The emoji as a unicode emoji.
     */
    Optional<String> asUnicodeEmoji();

    /**
     * Gets the emoji as a custom emoji.
     *
     * @return The emoji as a custom emoji.
     */
    default Optional<CustomEmoji> asCustomEmoji() {
        return as(CustomEmoji.class);
    }

    /**
     * Gets the emoji as a known custom emoji.
     *
     * @return The emoji as a known custom emoji.
     */
    default Optional<KnownCustomEmoji> asKnownCustomEmoji() {
        return as(KnownCustomEmoji.class);
    }

    /**
     * Checks if the emoji is equal to the given emoji.
     * This can be used to save some ugly optional checks.
     *
     * @param otherEmoji The emoji to compare with.
     * @return Whether the emoji is equal to the given emoji.
     */
    default boolean equalsEmoji(Emoji otherEmoji) {
        if (otherEmoji.isUnicodeEmoji()) {
            return equalsEmoji(otherEmoji.asUnicodeEmoji().orElse(""));
        }
        if (isUnicodeEmoji()) {
            // This is an unicode emoji and the other emoji is a custom emoji
            return false;
        }
        // Both are custom emojis, so we have to compare the id
        long thisId = asCustomEmoji().map(CustomEmoji::getId).orElseThrow(AssertionError::new);
        long otherId = otherEmoji.asCustomEmoji().map(CustomEmoji::getId).orElseThrow(AssertionError::new);
        return thisId == otherId;
    }

    /**
     * Checks if the emoji is equal to the given unicode emoji.
     * This can be used to save some ugly optional checks.
     *
     * @param otherEmoji The unicode emoji to compare with.
     * @return Whether the emoji is equal to the given unicode emoji.
     */
    default boolean equalsEmoji(String otherEmoji) {
        return asUnicodeEmoji()
                .map(emoji -> emoji.equals(otherEmoji))
                .orElse(false);
    }

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
