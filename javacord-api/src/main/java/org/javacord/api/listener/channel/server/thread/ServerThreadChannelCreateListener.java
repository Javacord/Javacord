package org.javacord.api.listener.channel.server.thread;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.thread.ThreadCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to thread creations.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerThreadChannelCreateListener extends GloballyAttachableListener,
        ServerThreadChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a thread is created.
     *
     * @param event The event.
     */
    void onThreadCreate(ThreadCreateEvent event);
}
