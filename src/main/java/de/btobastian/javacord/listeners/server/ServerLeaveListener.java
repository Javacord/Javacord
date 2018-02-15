package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerLeaveEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;

/**
 * This listener listens to server leaves.
 * Do not confuse it with the ServerMemberRemoveListener:
 * ServerMemberRemoveListener is for other users and ServerLeaveListener is for yourself!
 */
@FunctionalInterface
public interface ServerLeaveListener extends ServerAttachableListener, GloballyAttachableListener,
                                             ObjectAttachableListener {

    /**
     * This method is called every time you leave a server.
     *
     * @param event the event.
     */
    void onServerLeave(ServerLeaveEvent event);
}
