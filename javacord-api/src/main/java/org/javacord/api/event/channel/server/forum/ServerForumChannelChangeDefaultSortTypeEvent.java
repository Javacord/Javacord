package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.forum.SortOrderType;

/**
 * An event signaling that a server forum channel's default sort type has changed.
 */
public interface ServerForumChannelChangeDefaultSortTypeEvent extends ServerForumChannelEvent {

    /**
     * Gets the new default sort type of the channel.
     *
     * @return The new default sort type of the channel.
     */
    SortOrderType getNewDefaultSortType();

    /**
     * Gets the old default sort type of the channel.
     *
     * @return The old default sort type of the channel.
     */
    SortOrderType getOldDefaultSortType();
}
