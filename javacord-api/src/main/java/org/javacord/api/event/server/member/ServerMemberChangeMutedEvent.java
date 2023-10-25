package org.javacord.api.event.server.member;

import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user change muted event.
 */
public interface ServerMemberChangeMutedEvent extends ServerMemberEvent {

    /**
     * Gets the new muted state of the user.
     *
     * @return Whether the user is muted now.
     */
    boolean isNewMuted();

    /**
     * Gets the old muted state of the user.
     *
     * @return Whether the user was muted previously.
     */
    boolean isOldMuted();

}
