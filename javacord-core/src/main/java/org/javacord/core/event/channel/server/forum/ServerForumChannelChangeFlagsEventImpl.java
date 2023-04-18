package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ChannelFlag;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeFlagsEvent;

import java.util.EnumSet;

/**
 * The implementation of {@link ServerForumChannelChangeFlagsEvent}.
 */
public class ServerForumChannelChangeFlagsEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeFlagsEvent {

    /**
     * The old flags of the channel.
     */
    private final EnumSet<ChannelFlag> oldFlags;

    /**
     * The new flags of the channel.
     */
    private final EnumSet<ChannelFlag> newFlags;

    /**
     * Creates a new server forum channel change flags event.
     *
     * @param channel The channel of the event.
     * @param oldFlags The old flags of the channel.
     * @param newFlags The new flags of the channel.
     */
    public ServerForumChannelChangeFlagsEventImpl(ServerForumChannel channel, EnumSet<ChannelFlag> oldFlags,
                                                  EnumSet<ChannelFlag> newFlags) {
        super(channel);
        this.oldFlags = oldFlags;
        this.newFlags = newFlags;
    }

    @Override
    public EnumSet<ChannelFlag> getOldFlags() {
        return oldFlags;
    }

    @Override
    public EnumSet<ChannelFlag> getNewFlags() {
        return newFlags;
    }
}
