package org.javacord.api.event.server;

import org.javacord.api.entity.channel.ServerTextChannel;

import java.util.Optional;

/**
 * A server change system channel event.
 */
public interface ServerChangeSystemChannelEvent extends ServerEvent {

    /**
     * Gets the old system channel of the server.
     *
     * @return The old system channel of the server.
     */
    Optional<ServerTextChannel> getOldSystemChannel();

    /**
     * Gets the new system channel of the server.
     *
     * @return The new system channel of the server.
     */
    Optional<ServerTextChannel> getNewSystemChannel();

}
