package de.btobastian.javacord.listeners.server.member;

import de.btobastian.javacord.events.server.member.ServerMemberUnbanEvent;

/**
 * This listener listens to server member unbans.
 */
@FunctionalInterface
public interface ServerMemberUnbanListener {

    /**
     * This method is called every time a user got unbanned from a server.
     *
     * @param event The event.
     */
    void onServerMemberUnban(ServerMemberUnbanEvent event);
}
