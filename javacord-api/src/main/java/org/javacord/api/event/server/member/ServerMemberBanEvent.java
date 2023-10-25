package org.javacord.api.event.server.member;

import org.javacord.api.entity.server.Ban;

import java.util.concurrent.CompletableFuture;

/**
 * A server member ban event.
 */
public interface ServerMemberBanEvent extends ServerUserEvent {

    /**
     * Requests the Ban object of the event.
     *
     * @return The ban object.
     */
    CompletableFuture<Ban> requestBan();
}
