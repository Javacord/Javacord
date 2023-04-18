package org.javacord.api.entity.channel.forum;

import java.util.Optional;

/**
 * This class represents a default reaction.
 */
public interface DefaultReaction {
    /**
     * Gets the id of a guild's custom emoji.
     *
     * @return The id of a guild's custom emoji.
     */
    Optional<Long> getEmojiId();

    /**
     * Gets the id of a guild's custom emoji as a string.
     *
     * @return The id of a guild's custom emoji as a string.
     */
    default Optional<String> getEmojiIdAsString() {
        return getEmojiId().map(Long::toUnsignedString);
    }

    /**
     * Gets the name of a guild's custom emoji.
     *
     * @return The name of a guild's custom emoji.
     */
    Optional<String> getEmojiName();
}
