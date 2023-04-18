package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangePositionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server forum channel position changes.
 */
public interface ServerForumChannelChangePositionListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's position changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangePosition(ServerForumChannelChangePositionEvent event);
}
