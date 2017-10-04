package de.btobastian.javacord.entities.message.emoji;

import de.btobastian.javacord.entities.Mentionable;

import java.util.Optional;

/**
 * This class represents an emoji which can be a custom emoji or a unicode emoji.
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

}
