package org.javacord.api.entity.channel.forum;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Nameable;

import java.util.Optional;

/**
 * This class represents a forum tag.
 */
public interface ForumTag extends DiscordEntity, Nameable {
    /**
     * Gets whether this tag can only be added to or removed from a thread by a moderator.
     *
     * @return Whether this tag is locked to MANAGE_THREADS members
     */
    boolean isModerated();

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
