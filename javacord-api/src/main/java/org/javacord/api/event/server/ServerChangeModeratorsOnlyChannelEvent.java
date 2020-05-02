package org.javacord.api.event.server;

import org.javacord.api.entity.channel.ServerTextChannel;

import java.util.Optional;

/**
 * A server change moderators-only channel event.
 */
public interface ServerChangeModeratorsOnlyChannelEvent extends ServerEvent {

    /**
     * Gets the old moderators-only channel of the server.
     *
     * @return The old moderators-only channel of the server.
     */
    Optional<ServerTextChannel> getOldModeratorsOnlyChannel();

    /**
     * Gets the new moderators-only channel of the server.
     *
     * @return The new moderators-only channel of the server.
     */
    Optional<ServerTextChannel> getNewModeratorsOnlyChannel();
}
