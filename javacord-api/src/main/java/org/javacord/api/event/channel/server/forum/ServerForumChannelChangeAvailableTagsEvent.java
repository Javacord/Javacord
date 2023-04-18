package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.forum.AvailableTag;

import java.util.List;

/**
 * A server forum channel change tags event.
 */
public interface ServerForumChannelChangeAvailableTagsEvent extends ServerForumChannelEvent {

    /**
     * Gets the old tags.
     *
     * @return The old tags.
     */
    List<AvailableTag> getOldTags();

    /**
     * Gets the new tags.
     *
     * @return The new tags.
     */
    List<AvailableTag> getNewTags();

}
