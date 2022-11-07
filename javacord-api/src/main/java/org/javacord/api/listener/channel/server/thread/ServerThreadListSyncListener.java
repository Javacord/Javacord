package org.javacord.api.listener.channel.server.thread;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.thread.ThreadListSyncEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to thread list syncs.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerThreadListSyncListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a user gains access to a channel.
     *
     * @param event The event.
     */
    void onThreadListSync(ThreadListSyncEvent event);
}
