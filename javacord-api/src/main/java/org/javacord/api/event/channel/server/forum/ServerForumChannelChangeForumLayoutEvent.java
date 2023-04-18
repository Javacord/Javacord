package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.forum.ForumLayoutType;

/**
 * A server forum channel change forum layout event.
 */
public interface ServerForumChannelChangeForumLayoutEvent extends ServerForumChannelEvent {

    /**
     * Gets the old forum layout type.
     *
     * @return The old forum layout type.
     */
    ForumLayoutType getOldForumLayoutType();

    /**
     * Gets the new forum layout type.
     *
     * @return The new forum layout type.
     */
    ForumLayoutType getNewForumLayoutType();
}
