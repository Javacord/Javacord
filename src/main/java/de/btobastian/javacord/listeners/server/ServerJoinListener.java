package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerJoinEvent;
import de.btobastian.javacord.listeners.server.member.ServerMemberJoinListener;

/**
 * This listener listens to server joins.
 * Do not confuse it with the {@link ServerMemberJoinListener}:
 * ServerMemberAddListener is for other users and ServerJoinListener is for yourself!
 */
@FunctionalInterface
public interface ServerJoinListener {

    /**
     * This method is called every time you join a server.
     *
     * @param event The event.
     */
    void onServerJoin(ServerJoinEvent event);
}
