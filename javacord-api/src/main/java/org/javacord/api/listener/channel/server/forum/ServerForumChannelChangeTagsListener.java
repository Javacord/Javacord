package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeTagsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to channel flag changes.
 */
@FunctionalInterface
public interface ServerForumChannelChangeTagsListener extends GloballyAttachableListener,
        ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a channel's tags change.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeTags(ServerForumChannelChangeTagsEvent event);
}
