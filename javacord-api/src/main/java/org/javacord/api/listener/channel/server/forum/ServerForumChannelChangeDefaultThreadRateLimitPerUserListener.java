package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This is a marker interface for all server forum channel change default thread rate limit per user
 * listeners.
 */
public interface ServerForumChannelChangeDefaultThreadRateLimitPerUserListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's default thread rate limit per user.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeDefaultThreadRateLimitPerUser(
            ServerForumChannelChangeDefaultThreadRateLimitPerUserEvent event);
}
