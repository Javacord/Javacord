package org.javacord.api.entity.server.invite;

import java.util.Optional;

/**
 * This class represents a Welcome Screen Channel.
 */
public interface WelcomeScreenChannel {

    /**
     * Gets the channel's id.
     * 
     * @return the channel's id
     */
    long getChannelId();

    /**
     * Gets the description shown for the channel.
     * 
     * @return the description shown for the channel.
     */
    String getDescription();

    /**
     * Gets the emoji id, if the emoji is custom.
     * 
     * @return the emoji id, if the emoji is custom.
     */
    Optional<Long> getEmojiId();

    /**
     * Gets the emoji name if custom or the unicode character.
     * 
     * @return the emoji name if custom or the unicode character.
     */
    Optional<String> getEmojiName();
}
