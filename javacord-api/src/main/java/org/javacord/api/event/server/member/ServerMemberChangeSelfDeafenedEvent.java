package org.javacord.api.event.server.member;

import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user change self-deafened event.
 */
public interface ServerMemberChangeSelfDeafenedEvent extends ServerMemberEvent {

    /**
     * Gets the new self-deafened state of the user.
     *
     * @return Whether the user is self-deafened now.
     */
    boolean isNewSelfDeafened();

    /**
     * Gets the old self-deafened state of the user.
     *
     * @return Whether the user was self-deafened previously.
     */
    boolean isOldSelfDeafened();

}
