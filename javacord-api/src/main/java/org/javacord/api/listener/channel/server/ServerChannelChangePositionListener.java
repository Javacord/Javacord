package org.javacord.api.listener.channel.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.ServerChannelChangePositionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server channel position changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChannelChangePositionListener extends ServerAttachableListener, ServerChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server channel's position changes.
     *
     * @param event The event.
     */
    void onServerChannelChangePosition(ServerChannelChangePositionEvent event);
}
