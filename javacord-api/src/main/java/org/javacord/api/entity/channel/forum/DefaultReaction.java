package org.javacord.api.entity.channel.forum;

/**
 * This class represents a default reaction.
 */
public interface DefaultReaction {
    /**
     * Gets the id of a guild's custom emoji.
     *
     * @return The id of a guild's custom emoji.
     */
    long getEmojiId();

    /**
     * Gets the id of a guild's custom emoji as a string.
     *
     * @return The id of a guild's custom emoji as a string.
     */
    default String getEmojiIdAsString() {
        return Long.toUnsignedString(getEmojiId());
    }

    /**
     * Gets the name of a guild's custom emoji.
     *
     * @return The name of a guild's custom emoji.
     */
    String getEmojiName();
}
