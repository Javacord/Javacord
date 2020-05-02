package org.javacord.api.event.server;

import org.javacord.api.entity.channel.ServerTextChannel;

import java.util.Optional;

/**
 * A server change rules channel event.
 */
public interface ServerChangeRulesChannelEvent {

    /**
     * Gets the old rules channel of the server.
     *
     * @return The old rules channel of the server.
     */
    Optional<ServerTextChannel> getOldRulesChannel();

    /**
     * Gets the new rules channel of the server.
     *
     * @return The new rules channel of the server.
     */
    Optional<ServerTextChannel> getNewRulesChannel();

}
