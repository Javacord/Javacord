package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.SortOrderType;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultSortTypeEvent;

/**
 * The implementation of {@link ServerForumChannelChangeDefaultSortTypeEvent}.
 */
public class ServerForumChannelChangeDefaultSortTypeEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeDefaultSortTypeEvent {

    /**
     * The old default sort type of the channel.
     */
    private final SortOrderType oldDefaultSortType;

    /**
     * The new default sort type of the channel.
     */
    private final SortOrderType newDefaultSortType;

    /**
     * Creates a new server forum channel change default sort type event.
     *
     * @param channel The channel of the event.
     * @param oldDefaultSortType The old default sort type of the channel.
     * @param newDefaultSortType The new default sort type of the channel.
     */
    public ServerForumChannelChangeDefaultSortTypeEventImpl(final ServerForumChannel channel,
                                                            final SortOrderType oldDefaultSortType,
                                                            final SortOrderType newDefaultSortType) {
        super(channel);
        this.oldDefaultSortType = oldDefaultSortType;
        this.newDefaultSortType = newDefaultSortType;
    }

    @Override
    public SortOrderType getOldDefaultSortType() {
        return oldDefaultSortType;
    }

    @Override
    public SortOrderType getNewDefaultSortType() {
        return newDefaultSortType;
    }
}
