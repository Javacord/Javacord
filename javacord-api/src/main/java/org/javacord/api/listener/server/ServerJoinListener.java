package org.javacord.api.listener.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server joins.
 * Do not confuse it with the {@link ServerMemberJoinListener}:
 * {@code ServerMemberJoinListener} is for other users and {@code ServerJoinListener} is for yourself!
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerJoinListener extends GloballyAttachableListener {

    /**
     * This method is called every time you join a server.
     *
     * @param event The event.
     */
    void onServerJoin(ServerJoinEvent event);
}
