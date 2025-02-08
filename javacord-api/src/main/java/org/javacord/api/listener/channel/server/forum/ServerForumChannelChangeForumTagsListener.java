package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeForumTagsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to forum tags changes.
 */
@FunctionalInterface
public interface ServerForumChannelChangeForumTagsListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a channel's tags change.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeAvailableTags(ServerForumChannelChangeForumTagsEvent event);
}
