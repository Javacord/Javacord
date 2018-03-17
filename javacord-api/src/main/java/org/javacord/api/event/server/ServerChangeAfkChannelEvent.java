package org.javacord.api.event.server;

import org.javacord.api.entity.channel.ServerVoiceChannel;

import java.util.Optional;

/**
 * A server change afk channel event.
 */
public interface ServerChangeAfkChannelEvent extends ServerEvent {

    /**
     * Gets the old afk channel of the server.
     *
     * @return The old afk channel of the server.
     */
    Optional<ServerVoiceChannel> getOldAfkChannel();

    /**
     * Gets the new afk channel of the server.
     *
     * @return The new afk channel of the server.
     */
    Optional<ServerVoiceChannel> getNewAfkChannel();

}
