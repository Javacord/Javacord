package org.javacord.api.event.channel.server.thread;

import org.javacord.api.entity.channel.forum.ForumTag;

import java.util.Set;

/**
 * A server thread channel change applied tag ids event.
 */
public interface ServerThreadChannelChangeForumTagsEvent extends ServerThreadChannelEvent {

    /**
     * Gets the old forum tags of the channel.
     *
     * @return The old forum tags of the channel.
     */
    Set<ForumTag> getOldForumTags();

    /**
     * Gets the new forum tags of the channel.
     *
     * @return The new forum tags of the channel.
     */
    Set<ForumTag> getNewForumTags();
}
