package org.javacord.api.event.server.member;

import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user change self-muted event.
 */
public interface ServerMemberChangeSelfMutedEvent extends ServerMemberEvent {

    /**
     * Gets the new self-muted state of the user.
     *
     * @return Whether the user is self-muted now.
     */
    boolean isNewSelfMuted();

    /**
     * Gets the old self-muted state of the user.
     *
     * @return Whether the user was self-muted previously.
     */
    boolean isOldSelfMuted();

}
