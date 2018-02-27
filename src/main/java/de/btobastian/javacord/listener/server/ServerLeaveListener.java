package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerLeaveEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

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
