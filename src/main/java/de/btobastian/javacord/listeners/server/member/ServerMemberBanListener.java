package de.btobastian.javacord.listeners.server.member;

import de.btobastian.javacord.events.server.member.ServerMemberBanEvent;

/**
 * This listener listens to server member bans.
 */
@FunctionalInterface
public interface ServerMemberBanListener {

    /**
     * This method is called every time a user got banned from a server.
     *
     * @param event The event.
     */
    void onServerMemberBan(ServerMemberBanEvent event);
}
