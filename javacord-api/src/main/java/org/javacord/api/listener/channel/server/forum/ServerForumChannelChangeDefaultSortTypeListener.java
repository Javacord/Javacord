package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultSortTypeEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server forum channel default sort type changes.
 */
public interface ServerForumChannelChangeDefaultSortTypeListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's default sort type changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeDefaultSortType(ServerForumChannelChangeDefaultSortTypeEvent event);
}
