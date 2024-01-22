package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeVersionEvent;

/**
 * The implementation of {@link ServerForumChannelChangeVersionEvent}.
 */
public class ServerForumChannelChangeVersionEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeVersionEvent {

    /**
     * The old version of the channel.
     */
    private final long oldVersion;

    /**
     * The new version of the channel.
     */
    private final long newVersion;

    /**
     * Creates a new server forum channel change version event.
     *
     * @param channel The channel of the event.
     * @param oldVersion The old version of the channel.
     * @param newVersion The new version of the channel.
     */
    public ServerForumChannelChangeVersionEventImpl(final ServerForumChannel channel,
                                                    final long oldVersion, final long newVersion) {
        super(channel);
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    @Override
    public long getOldVersion() {
        return oldVersion;
    }

    @Override
    public long getNewVersion() {
        return newVersion;
    }
}

