package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerJoinEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.server.member.ServerMemberJoinListener;

/**
 * This listener listens to server joins.
 * Do not confuse it with the {@link ServerMemberJoinListener}:
 * ServerMemberAddListener is for other users and ServerJoinListener is for yourself!
 */
@FunctionalInterface
public interface ServerJoinListener extends GloballyAttachableListener {

    /**
     * This method is called every time you join a server.
     *
     * @param event The event.
     */
    void onServerJoin(ServerJoinEvent event);
}
