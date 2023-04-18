package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeRateLimitPerUserEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server forum channel rate limit per user changes.
 */
public interface ServerForumChannelChangeRateLimitPerUserListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's rate limit per user changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeRateLimitPerUser(ServerForumChannelChangeRateLimitPerUserEvent event);
}
