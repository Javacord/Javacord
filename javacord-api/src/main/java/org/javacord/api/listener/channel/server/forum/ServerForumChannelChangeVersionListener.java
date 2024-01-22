package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeVersionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to version changes of a server forum channel.
 */
public interface ServerForumChannelChangeVersionListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's version changed.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeVersion(ServerForumChannelChangeVersionEvent event);
}
