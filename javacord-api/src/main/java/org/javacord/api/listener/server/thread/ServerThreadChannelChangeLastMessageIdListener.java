package org.javacord.api.listener.server.thread;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeLastMessageIdEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server thread channel last message id change events.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerThreadChannelChangeLastMessageIdListener extends ServerThreadChannelAttachableListener,
        ServerAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server thread's last message id changed.
     *
     * @param event The event.
     */
    void onServerThreadChannelChangeLastMessageId(ServerThreadChannelChangeLastMessageIdEvent event);
}
