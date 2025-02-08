package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.forum.ForumTag;

import java.util.Set;

/**
 * A server forum channel change forum tags event.
 */
public interface ServerForumChannelChangeForumTagsEvent extends ServerForumChannelEvent {

    /**
     * Gets the old forum tags.
     *
     * @return The old forum tags.
     */
    Set<ForumTag> getOldForumTags();

    /**
     * Gets the new forum tags.
     *
     * @return The new forum tags.
     */
    Set<ForumTag> getNewForumTags();
}
