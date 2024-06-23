package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.ForumLayoutType;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeForumLayoutEvent;

/**
 * The implementation of {@link ServerForumChannelChangeForumLayoutEvent}.
 */
public class ServerForumChannelChangeForumLayoutEventImpl  extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeForumLayoutEvent {

    /**
     * The old forum layout type of the channel.
     */
    private final ForumLayoutType oldForumLayoutType;

    /**
     * The new forum layout type of the channel.
     */
    private final ForumLayoutType newForumLayoutType;

    /**
     * Creates a new server forum channel change forum layout event.
     *
     * @param channel The channel of the event.
     * @param oldForumLayoutType The old forum layout type of the channel.
     * @param newForumLayoutType The new forum layout type of the channel.
     */
    public ServerForumChannelChangeForumLayoutEventImpl(final ServerForumChannel channel,
                                                        final ForumLayoutType oldForumLayoutType,
                                                        final ForumLayoutType newForumLayoutType) {
        super(channel);
        this.oldForumLayoutType = oldForumLayoutType;
        this.newForumLayoutType = newForumLayoutType;
    }

    @Override
    public ForumLayoutType getOldForumLayoutType() {
        return oldForumLayoutType;
    }

    @Override
    public ForumLayoutType getNewForumLayoutType() {
        return newForumLayoutType;
    }
}
