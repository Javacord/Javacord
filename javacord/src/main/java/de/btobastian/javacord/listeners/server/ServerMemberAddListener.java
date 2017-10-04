package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerMemberAddEvent;

/**
 * This listener listens to server member joins.
 * Do not confuse it with the {@link ServerJoinListener}:
 * ServerMemberAddListener is for other users and ServerJoinListener is for yourself!
 */
@FunctionalInterface
public interface ServerMemberAddListener {

    /**
     * This method is called every time a user joins a server.
     *
     * @param event The event.
     */
    void onServerMemberAdd(ServerMemberAddEvent event);
}
