package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.PermissionOverwrite;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangePermissionOverwritesEvent;

import java.util.List;

/**
 * The implementation of {@link ServerForumChannelChangePermissionOverwritesEvent}.
 */
public class ServerForumChannelChangePermissionOverwritesEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangePermissionOverwritesEvent {

    /**
     * The old permission overwrites of the channel.
     */
    private final List<PermissionOverwrite> oldPermissionOverwrites;

    /**
     * The new permission overwrites of the channel.
     */
    private final List<PermissionOverwrite> newPermissionOverwrites;

    /**
     * Creates a new server forum channel change permission overwrites event.
     *
     * @param channel                 The channel of the event.
     * @param oldPermissionOverwrites The old permission overwrites of the channel.
     * @param newPermissionOverwrites The new permission overwrites of the channel.
     */
    public ServerForumChannelChangePermissionOverwritesEventImpl(
            final ServerForumChannel channel, final List<PermissionOverwrite> oldPermissionOverwrites,
            final List<PermissionOverwrite> newPermissionOverwrites) {
        super(channel);
        this.oldPermissionOverwrites = oldPermissionOverwrites;
        this.newPermissionOverwrites = newPermissionOverwrites;
    }

    @Override
    public List<PermissionOverwrite> getOldPermissionOverwrites() {
        return oldPermissionOverwrites;
    }

    @Override
    public List<PermissionOverwrite> getNewPermissionOverwrites() {
        return newPermissionOverwrites;
    }
}
