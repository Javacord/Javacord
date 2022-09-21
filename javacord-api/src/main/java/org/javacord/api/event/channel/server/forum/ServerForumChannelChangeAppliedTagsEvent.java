package org.javacord.api.event.channel.server.forum;

import java.util.List;

/**
 * A server forum channel change applied tags event.
 */
public interface ServerForumChannelChangeAppliedTagsEvent extends ServerForumChannelEvent {

    /**
     * Gets the old applied tags.
     *
     * @return The old applied tags.
     */
    List<Long> getOldAppliedTags();

    /**
     * Gets the new applied tags.
     *
     * @return The new applied tags.
     */
    List<Long> getNewAppliedTags();

}
