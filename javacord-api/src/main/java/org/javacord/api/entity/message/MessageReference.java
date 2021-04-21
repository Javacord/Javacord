package org.javacord.api.entity.message;

import java.util.Optional;

/**
 * This class represents a reference to another message like a cross-posted / channel follow / pin / reply message.
 */
public interface MessageReference {

    /**
     * Gets the id of the originating message.
     *
     * @return the message id.
     */
    Optional<Long> getMessageId();

    /**
     * Gets the id of the originating message's channel.
     *
     * @return the channel id.
     */
    Optional<Long> getChannelId();

    /**
     * Gets the id of the originating message's server.
     *
     * @return the server id.
     */
    Optional<Long> getServerId();

}
