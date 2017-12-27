package de.btobastian.javacord.listeners.server.member;

import de.btobastian.javacord.events.server.member.ServerMemberLeaveEvent;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;

/**
 * This listener listens to server member leaves.
 * Do not confuse it with the {@link ServerLeaveListener}:
 * ServerMemberRemoveListener is for other users and ServerLeaveListener is for yourself!
 */
@FunctionalInterface
public interface ServerMemberLeaveListener {

    /**
    * This method is called every time a user leaves a server.
    *
    * @param event the event.
    */
    void onServerMemberLeave(ServerMemberLeaveEvent event);
}
