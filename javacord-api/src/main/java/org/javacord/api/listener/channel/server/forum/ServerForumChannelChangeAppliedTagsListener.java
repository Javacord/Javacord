package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeAppliedTagsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to applied tags changes.
 */
public interface ServerForumChannelChangeAppliedTagsListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a tag is added or removed.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeAppliedTags(ServerForumChannelChangeAppliedTagsEvent event);
}
