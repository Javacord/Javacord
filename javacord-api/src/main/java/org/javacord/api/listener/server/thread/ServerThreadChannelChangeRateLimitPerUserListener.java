package org.javacord.api.listener.server.thread;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeRateLimitPerUserEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server thread channel rate limit per user changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerThreadChannelChangeRateLimitPerUserListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread channel's rate limit per user changes.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeRateLimitPerUser(ServerThreadChannelChangeRateLimitPerUserEvent event);
}
