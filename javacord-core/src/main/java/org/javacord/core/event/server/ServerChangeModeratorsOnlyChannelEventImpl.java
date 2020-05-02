package org.javacord.core.event.server;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.server.ServerChangeModeratorsOnlyChannelEvent;
import org.javacord.core.entity.server.ServerImpl;

import java.util.Optional;

/**
 * The implementation of {@link ServerChangeModeratorsOnlyChannelEvent}.
 */
public class ServerChangeModeratorsOnlyChannelEventImpl
        extends ServerEventImpl implements ServerChangeModeratorsOnlyChannelEvent {

    /**
     * The new moderators-only channel of the server.
     */
    private final ServerTextChannel newModeratorsOnlyChannel;

    /**
     * The old moderator-only channel of the server.
     */
    private final ServerTextChannel oldModeratorsOnlyChannel;

    /**
     * Creates a new moderators-only channel change event.
     *
     * @param server                  The server of the event.
     * @param newModeratorsOnlyChannel The new moderators-only channel of the server.
     * @param oldModeratorsOnlyChannel The old moderators-only updates channel of the server.
     */
    public ServerChangeModeratorsOnlyChannelEventImpl(ServerImpl server,
                                                      ServerTextChannel newModeratorsOnlyChannel,
                                                      ServerTextChannel oldModeratorsOnlyChannel) {
        super(server);
        this.newModeratorsOnlyChannel = newModeratorsOnlyChannel;
        this.oldModeratorsOnlyChannel = oldModeratorsOnlyChannel;
    }


    @Override
    public Optional<ServerTextChannel> getOldModeratorsOnlyChannel() {
        return Optional.ofNullable(oldModeratorsOnlyChannel);
    }

    @Override
    public Optional<ServerTextChannel> getNewModeratorsOnlyChannel() {
        return Optional.ofNullable(newModeratorsOnlyChannel);
    }
}
