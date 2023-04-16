package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server leaves.
 * Do not confuse it with the ServerMemberRemoveListener:
 {@code ServerMemberLeaveListener} is for other users and {@code ServerLeaveListener} is for yourself!
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerLeaveListener extends ServerAttachableListener, GloballyAttachableListener,
                                             ObjectAttachableListener {

    /**
     * This method is called every time you leave a server.
     *
     * @param event the event.
     */
    void onServerLeave(ServerLeaveEvent event);
}
