package org.javacord.api.event.user;

import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user pending state (membership screening) change event.
 */
public interface UserChangePendingEvent extends ServerMemberEvent {

    /**
     * Gets the old pending state of the member.
     *
     * @return The old pending state of the member.
     */
    boolean getOldPending();

    /**
     * Gets the new pending state of the member.
     *
     * @return The new pending state of the member.
     */
    boolean getNewPending();

}
