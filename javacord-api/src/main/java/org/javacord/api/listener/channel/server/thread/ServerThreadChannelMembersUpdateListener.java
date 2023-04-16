package org.javacord.api.listener.channel.server.thread;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.thread.ThreadMembersUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to thread members updates.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerThreadChannelMembersUpdateListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener, ServerThreadChannelAttachableListener {

    /**
     * This method is called every time someone is added to or removed from a thread.
     *
     * @param event The event.
     */
    void onThreadMembersUpdate(ThreadMembersUpdateEvent event);
}
