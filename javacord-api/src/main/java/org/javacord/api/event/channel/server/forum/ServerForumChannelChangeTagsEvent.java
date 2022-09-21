package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.forum.ForumTag;

import java.util.List;

/**
 * A server forum channel change tags event.
 */
public interface ServerForumChannelChangeTagsEvent extends ServerForumChannelEvent {

    /**
     * Gets the old tags.
     *
     * @return The old tags.
     */
    List<ForumTag> getOldTags();

    /**
     * Gets the new tags.
     *
     * @return The new tags.
     */
    List<ForumTag> getNewTags();

}
