package de.btobastian.javacord.listeners.server.member;

import de.btobastian.javacord.events.server.member.ServerMemberJoinEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.server.ServerJoinListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

/**
 * This listener listens to server member joins.
 * Do not confuse it with the {@link ServerJoinListener}:
 * ServerMemberAddListener is for other users and ServerJoinListener is for yourself!
 */
@FunctionalInterface
public interface ServerMemberJoinListener extends ServerAttachableListener, UserAttachableListener,
                                                  GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user joins a server.
     *
     * @param event The event.
     */
    void onServerMemberJoin(ServerMemberJoinEvent event);
}
