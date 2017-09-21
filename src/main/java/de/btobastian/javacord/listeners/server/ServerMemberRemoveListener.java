package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerMemberRemoveEvent;

/**
 * This listener listens to server member leaves.
 * Do not confuse it with the {@link ServerLeaveListener}:
 * ServerMemberRemoveListener is for other users and ServerLeaveListener is for yourself!
 */
@FunctionalInterface
public interface ServerMemberRemoveListener {

    /**
    * This method is called every time a user leaves a server.
    *
    * @param event the event.
    */
    void onServerMemberRemove(ServerMemberRemoveEvent event);
}
